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
import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier.INLINE
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.UNIT
import com.squareup.kotlinpoet.WildcardTypeName
import com.squareup.kotlinpoet.asClassName
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs
import sun.reflect.generics.reflectiveObjects.NotImplementedException
import java.io.File
import java.nio.file.Files
import kotlin.properties.Delegates

private val UNIT_OBSERVABLE = ParameterizedTypeName.get(ClassName("io.reactivex", "Observable"), UNIT)

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
        "com.jakewharton.rxbinding2.internal.Functions",
        "com.jakewharton.rxbinding2.internal.GenericTypeNullable"
    )

    private val GenericTypeNullableAnnotation = MarkerAnnotationExpr(
        NameExpr("GenericTypeNullable"))

    /** Recursive function for resolving a Type into a Kotlin-friendly String representation */
    fun resolveKotlinType(inputType: Type,
        methodAnnotations: List<AnnotationExpr>? = null): TypeName {
      return when (inputType) {
        is ReferenceType -> resolveKotlinType(inputType.type, methodAnnotations)
        is PrimitiveType -> resolveKotlinTypeByName(inputType.toString())
        is VoidType -> resolveKotlinTypeByName(inputType.toString())
        is ClassOrInterfaceType -> resolveKotlinClassOrInterfaceType(inputType, methodAnnotations)
        is WildcardType -> resolveKotlinWildcardType(inputType, methodAnnotations)
        else -> throw NotImplementedException()
      }
    }

    private fun resolveKotlinTypeByName(input: String): ClassName {
      return when (input) {
        "Object" -> ANY
        "Integer" -> INT
        "int", "char", "boolean", "long", "float", "short", "byte" -> {
          ClassName("kotlin", input.capitalize())
        }
        "List" -> MutableList::class.asClassName()
        else -> ClassName.bestGuess(input)
      }
    }

    private fun resolveKotlinClassOrInterfaceType(
        inputType: ClassOrInterfaceType,
        methodAnnotations: List<AnnotationExpr>?): TypeName {
      return if (isObservableObject(inputType)) {
        UNIT_OBSERVABLE
      } else {
        ParameterizedTypeName.get(resolveKotlinTypeByName(inputType.name),
            *resolveTypeArguments(inputType, methodAnnotations).toTypedArray())
      }
    }

    private fun isObservableObject(inputType: ClassOrInterfaceType): Boolean {
      return inputType.name == "Observable" &&
          inputType.typeArgs?.first() == ReferenceType(ClassOrInterfaceType("Object"))
    }

    private fun resolveTypeArguments(inputType: ClassOrInterfaceType,
        methodAnnotations: List<AnnotationExpr>?): List<TypeName> {
      return inputType.typeArgs?.map { type: Type ->
        resolveKotlinType(type, methodAnnotations)
      } ?: emptyList()
    }

    private fun resolveKotlinWildcardType(inputType: WildcardType,
        methodAnnotations: List<AnnotationExpr>?): WildcardTypeName {
      val isNullable = isWildcardNullable(methodAnnotations)
      return when {
        inputType.`super` != null -> WildcardTypeName.supertypeOf(
            resolveKotlinType(inputType.`super`))
            .let {
              if (isNullable) {
                it.asNullable()
              } else {
                it
              }
            }
        inputType.extends != null -> WildcardTypeName.subtypeOf(
            resolveKotlinType(inputType.extends))
            .let {
              if (isNullable) {
                it.asNullable()
              } else {
                it
              }
            }
        else -> throw IllegalStateException("Wildcard with no super or extends")
      }
    }

    private fun isWildcardNullable(annotations: List<AnnotationExpr>?): Boolean {
      return annotations?.firstOrNull { it == GenericTypeNullableAnnotation }?.let { true } == true
    }
  }

  @TaskAction
  @Suppress("unused")
  fun generate(inputs: IncrementalTaskInputs) {
    // Clear things out first to make sure no stragglers are left
    val outputDir = File("${project.projectDir}-kotlin${SLASH}src${SLASH}main${SLASH}kotlin")
    outputDir.walkTopDown()
        .filter { it.isFile }
        .filterNot { it.absolutePath.contains("internal") }
        .forEach { it.delete() }

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
        methods.firstOrNull { it.emitsUnit() }?.let {
          writer.append("import com.jakewharton.rxbinding2.internal.VoidToUnit\n")
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
    private val kotlinType = resolveKotlinType(returnType, annotations)

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
          .replace(
              "\\{@link ($DOC_LINK_REGEX)#($DOC_LINK_REGEX)\\}".toRegex()) { result: MatchResult ->
            val (foo, bar) = result.destructured
            "[$foo.$bar]"
          }
          // {@linkplain Foo baz} -> [baz][Foo]
          .replace(
              "\\{@linkplain ($DOC_LINK_REGEX) ($DOC_LINK_REGEX)\\}".toRegex()) { result: MatchResult ->
            val (foo, baz) = result.destructured
            "[$baz][$foo]"
          }
          //{@linkplain Foo#bar baz} -> [baz][Foo.bar]
          .replace(
              "\\{@linkplain ($DOC_LINK_REGEX)#($DOC_LINK_REGEX) ($DOC_LINK_REGEX)\\}".toRegex()) { result: MatchResult ->
            val (foo, bar, baz) = result.destructured
            "[$baz][$foo.$bar]"
          }
          // Remove any trailing whitespace
          .replace("(?m)\\s+$".toRegex(), "")
          .trim()
    }

    /** Generates method level type parameters */
    private fun typeParams(params: List<TypeParameter>?): List<TypeVariableName>? {
      return params?.map { p ->
        TypeVariableName(p.name, resolveKotlinType(p.typeBound[0]))
      }
    }

    /**
     * Generates parameters in a kotlin-style format
     */
    private fun kParams(): List<ParameterSpec> {
      return parameters.map { p ->
        ParameterSpec.builder(p.id.name, resolveKotlinType(p.type)).build()
      }
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

      val parameterSpecs = kParams()

      val builder = StringBuilder();

      // doc
      builder.append("${comment ?: ""}\n")

      // signature boilerplate
      builder.append("inline fun ")

      // type params
      builder.append(if (typeParameters != null) typeParameters + " " else "")

      // return type
      builder.append("$extendedClass.$name($fParams): $kotlinType")

      builder.append(" = ")

      // target method call
      builder.append(
          "$bindingClass.$name(${if (jParams.isNotEmpty()) "this, $jParams" else "this"})")

      // Object --> Unit mapping
      if (emitsUnit()) {
        builder.append(".map(VoidToUnit)")
      }

      return builder.toString()
    }

    fun emitsUnit() = kotlinType == UNIT_OBSERVABLE

  }
}
