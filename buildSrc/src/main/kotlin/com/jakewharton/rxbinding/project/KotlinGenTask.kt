package com.jakewharton.rxbinding.project

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.ImportDeclaration
import com.github.javaparser.ast.PackageDeclaration
import com.github.javaparser.ast.TypeParameter
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.expr.AnnotationExpr
import com.github.javaparser.ast.expr.MarkerAnnotationExpr
import com.github.javaparser.ast.expr.NameExpr
import com.github.javaparser.ast.type.ClassOrInterfaceType
import com.github.javaparser.ast.type.PrimitiveType
import com.github.javaparser.ast.type.ReferenceType
import com.github.javaparser.ast.type.Type
import com.github.javaparser.ast.type.VoidType
import com.github.javaparser.ast.type.WildcardType
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs
import sun.reflect.generics.reflectiveObjects.NotImplementedException
import java.io.File
import java.nio.file.Files
import kotlin.properties.Delegates

open class KotlinGenTask : SourceTask() {

  companion object {
    /** Regex used for finding references in javadoc links */
    private val DOC_LINK_REGEX = "[0-9A-Za-z._]*"

    private val SLASH = File.separator

    /**
     * These are imports of classes that Kotlin advises against using and are replaced in
     * {@link #resolveKotlinTypeByName}
     */
    private val IGNORED_IMPORTS = listOf(
        "java.util.List",
        "android.support.annotation.CheckResult",
        "android.support.annotation.NonNull",
        "android.support.annotation.RequiresApi",
        "com.jakewharton.rxbinding2.internal.GenericTypeNullable"
    )

    private val GenericTypeNullableAnnotation = MarkerAnnotationExpr(NameExpr("GenericTypeNullable"))

    fun resolveKotlinTypeByName(input: String): String {
      return when (input) {
        "Object" -> "Any"
        "Void" -> "Unit"
        "Integer" -> "Int"
        "int", "char", "boolean", "long", "float", "short", "byte" -> input.capitalize()
        "List" -> "MutableList"
        else -> input
      }
    }

    /** Recursive function for resolving a Type into a Kotlin-friendly String representation */
    fun resolveKotlinType(inputType: Type, methodAnnotations: List<AnnotationExpr>? = null): String {
      if (inputType is ReferenceType) {
        return resolveKotlinType(inputType.type, methodAnnotations)
      }
      else if (inputType is ClassOrInterfaceType) {
        val baseType = resolveKotlinTypeByName(inputType.name)
        if (inputType.typeArgs == null || inputType.typeArgs.isEmpty()) {
          return baseType
        }
        return "$baseType<${inputType.typeArgs.map { type: Type -> resolveKotlinType(type, methodAnnotations) }.joinToString()}>"
      } else if (inputType is PrimitiveType || inputType is VoidType) {
        return resolveKotlinTypeByName(inputType.toString())
      } else if (inputType is WildcardType) {
        var nullable = ""
        methodAnnotations
            ?.filter { it == GenericTypeNullableAnnotation }
            ?.forEach { nullable = "?" }

        if (inputType.`super` != null) {
          return "in ${resolveKotlinType(inputType.`super`)}$nullable"
        } else if (inputType.extends != null) {
          return "out ${resolveKotlinType(inputType.extends)}$nullable"
        } else {
          throw IllegalStateException("Wildcard with no super or extends")
        }
      } else {
        throw NotImplementedException()
      }
    }
  }

  @TaskAction
  @Suppress("unused")
  fun generate(inputs: IncrementalTaskInputs) {
    // Clear things out first to make sure no stragglers are left
    val outputDir = File("${project.projectDir}-kotlin${SLASH}src${SLASH}main${SLASH}kotlin")
    outputDir.deleteDir()

    // Let's get going
    getSource().forEach { generateKotlin(it) }
  }

  fun generateKotlin(file: File) {
    val outputPath = file.parent.replace("java", "kotlin")
        .replace("${SLASH}src", "-kotlin${SLASH}src")
        .substringBefore("com${SLASH}jakewharton")
    val outputDir = File(outputPath)

    // Start parsing the java files
    val cu = JavaParser.parse(file)

    val kClass = KFile()
    kClass.fileName = file.name.replace(".java", ".kt")

    // Visit the appropriate nodes and extract information
    cu.accept(object : VoidVisitorAdapter<KFile>() {

      override fun visit(n: PackageDeclaration, arg: KFile) {
        arg.packageName = n.name.toString()
        super.visit(n, arg)
      }

      override fun visit(n: ClassOrInterfaceDeclaration, arg: KFile) {
        arg.bindingClass = n.name
        arg.extendedClass = n.name.replace("Rx", "")
        super.visit(n, arg)
      }

      override fun visit(n: MethodDeclaration, arg: KFile) {
        arg.methods.add(KMethod(n))
        // Explicitly avoid going deeper, we only care about top level methods. Otherwise
        // we'd hit anonymous inner classes and whatnot
      }

      override fun visit(n: ImportDeclaration, arg: KFile) {
        if (!n.isStatic) {
          arg.imports.add(n.name.toString())
        }
        super.visit(n, arg)
      }

    }, kClass)

    kClass.generate(outputDir)
  }

  /**
   * Represents a kotlin file that corresponds to a Java file/class in an RxBinding module
   */
  class KFile {
    var fileName: String by Delegates.notNull<String>()
    var packageName: String by Delegates.notNull<String>()
    var bindingClass: String by Delegates.notNull<String>()
    var extendedClass: String by Delegates.notNull<String>()
    val methods = mutableListOf<KMethod>()
    val imports = mutableListOf<String>()

    /** Generates the code and writes it to the desired directory */
    fun generate(directory: File) {
      var directoryPath = directory.absolutePath
      var finalDir: File? = null
      if (!packageName.isEmpty()) {
        packageName.split('.').forEach {
          directoryPath += File.separator + it
        }
        finalDir = File(directoryPath)
        Files.createDirectories(finalDir.toPath())
      }

      File(finalDir, fileName).bufferedWriter().use { writer ->
        writer.append("package $packageName\n\n")

        imports.forEach { im ->
          if (!IGNORED_IMPORTS.contains(im)) {
            writer.append("import $im\n")
          }
        }

        methods.forEach { m ->
          writer.append("\n${m.generate(bindingClass)}\n")
        }
      }
    }
  }

  /**
   * Represents a method implementation that needs to be wired up in Kotlin
   */
  class KMethod(n: MethodDeclaration) {
    private val name = n.name
    private val annotations: List<AnnotationExpr> = n.annotations
    private val comment = n.comment?.toString()?.let { cleanUpDoc(it) }
    private val extendedClass = n.parameters[0].type.toString()
    private val parameters = n.parameters.subList(1, n.parameters.size)
    private val returnType = n.type
    private val typeParameters = typeParams(n.typeParameters)

    /** Cleans up the generated doc and translates some html to equivalent markdown for Kotlin docs */
    private fun cleanUpDoc(doc: String): String {
      return doc.replace("<em>", "*")
          .replace("</em>", "*")
          .replace("<p>", "")
          // JavaParser adds a couple spaces to the beginning of these for some reason
          .replace("   *", " *")
          // {@code view} -> `view`
          .replace("\\{@code ($DOC_LINK_REGEX)\\}".toRegex()) { result: MatchResult ->
            val codeName = result.destructured
            "`${codeName.component1()}`"
          }
          // {@link Foo} -> [Foo]
          .replace("\\{@link ($DOC_LINK_REGEX)\\}".toRegex()) { result: MatchResult ->
            val foo = result.destructured
            "[${foo.component1()}]"
          }
          // {@link Foo#bar} -> [Foo.bar]
          .replace("\\{@link ($DOC_LINK_REGEX)#($DOC_LINK_REGEX)\\}".toRegex()) { result: MatchResult ->
            val (foo, bar) = result.destructured
            "[$foo.$bar]"
          }
          // {@linkplain Foo baz} -> [baz][Foo]
          .replace("\\{@linkplain ($DOC_LINK_REGEX) ($DOC_LINK_REGEX)\\}".toRegex()) { result: MatchResult ->
            val (foo, baz) = result.destructured
            "[$baz][$foo]"
          }
          //{@linkplain Foo#bar baz} -> [baz][Foo.bar]
          .replace("\\{@linkplain ($DOC_LINK_REGEX)#($DOC_LINK_REGEX) ($DOC_LINK_REGEX)\\}".toRegex()) { result: MatchResult ->
            val (foo, bar, baz) = result.destructured
            "[$baz][$foo.$bar]"
          }
          // Remove any trailing whitespace
          .replace("(?m)\\s+$".toRegex(), "")
          .trim()
    }

    /** Generates method level type parameters */
    private fun typeParams(params: List<TypeParameter>?): String? {
      if (params == null || params.isEmpty()) {
        return null
      }

      val builder = StringBuilder()
      builder.append("<")
      params.forEach { p -> builder.append("${p.name} : ${resolveKotlinType(p.typeBound[0])}") }
      builder.append(">")
      return builder.toString()
    }

    /**
     * Generates parameters in a kotlin-style format
     *
     * @param specifyType boolean indicating whether or not to specify the type (i.e. we don't
     *        need the type when we're passing params into the underlying Java implementation)
     */
    private fun kParams(specifyType: Boolean): String {
      val builder = StringBuilder()
      parameters.forEach { p -> builder.append("${p.id.name}${if (specifyType) ": " + resolveKotlinType(p.type) else ""}") }
      return builder.toString()
    }

    /**
     * Generates the kotlin code for this method
     *
     * @param bindingClass name of the RxBinding class this is tied to
     */
    fun generate(bindingClass: String): String {
      ///////////////
      // STRUCTURE //
      ///////////////
      // Javadoc
      // public inline fun DrawerLayout.drawerOpen(): Observable<Boolean> = RxDrawerLayout.drawerOpen(this)
      // <access specifier> inline fun <extendedClass>.<name>(params): <type> = <bindingClass>.name(this, params)

      val fParams = kParams(true)
      val jParams = kParams(false)

      val builder = StringBuilder();

      // doc
      builder.append("${comment ?: ""}\n")

      // signature boilerplate
      builder.append("inline fun ")

      // type params
      builder.append(if (typeParameters != null) typeParameters + " " else "")

      // return type
      val kotlinType = resolveKotlinType(returnType, annotations)
      builder.append("$extendedClass.$name($fParams): $kotlinType")

      builder.append(" = ")

      // target method call
      builder.append("$bindingClass.$name(${if (jParams.isNotEmpty()) "this, $jParams" else "this"})")

      // Void --> Unit mapping
      if (kotlinType.equals("Observable<Unit>")) {
        builder.append(".map { Unit }")
      }

      return builder.toString()
    }
  }

  /**
   * Copied over from the Groovy implementation
   */
  fun File.deleteDir(): Boolean {
    if (!exists()) {
      return true
    } else if (!isDirectory) {
      return false
    } else {
      val files = listFiles()
      if (files == null) {
        return false
      } else {
        var result = true

        for (file in files) {
          if (file.isDirectory) {
            if (!file.deleteDir()) {
              result = false
            }
          } else if (!file.delete()) {
            result = false
          }
        }

        if (!delete()) {
          result = false
        }

        return result
      }
    }
  }
}
