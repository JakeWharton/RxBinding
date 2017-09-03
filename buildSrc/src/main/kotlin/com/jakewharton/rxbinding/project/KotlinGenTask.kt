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
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.AnnotationSpec.UseSiteTarget.FILE
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier.INLINE
import com.squareup.kotlinpoet.KotlinFile
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
import kotlin.properties.Delegates

private val UNIT_OBSERVABLE = ParameterizedTypeName.get(
    ClassName("io.reactivex", "Observable"), UNIT)

open class KotlinGenTask : SourceTask() {

  companion object {
    /** Regex used for finding references in javadoc links */
    private val DOC_LINK_REGEX = "[0-9A-Za-z._]*"

    private val SLASH = File.separator

    private val GenericTypeNullableAnnotation = MarkerAnnotationExpr(
        NameExpr("GenericTypeNullable"))

    /** Recursive function for resolving a Type into a Kotlin-friendly String representation */
    fun resolveKotlinType(inputType: Type,
        methodAnnotations: List<AnnotationExpr>? = null,
        associatedImports: Map<String, ClassName>? = null): TypeName {
      return when (inputType) {
        is ReferenceType -> resolveKotlinType(inputType.type, methodAnnotations, associatedImports)
        is PrimitiveType -> resolveKotlinTypeByName(inputType.toString(), associatedImports)
        is VoidType -> resolveKotlinTypeByName(inputType.toString(), associatedImports)
        is ClassOrInterfaceType -> resolveKotlinClassOrInterfaceType(inputType, methodAnnotations,
            associatedImports)
        is WildcardType -> resolveKotlinWildcardType(inputType, methodAnnotations,
            associatedImports)
        else -> throw NotImplementedException()
      }
    }

    private fun resolveKotlinTypeByName(input: String,
        associatedImports: Map<String, ClassName>? = null): ClassName {
      return when (input) {
        "Object" -> ANY
        "Integer" -> INT
        "int", "char", "boolean", "long", "float", "short", "byte" -> {
          ClassName("kotlin", input.capitalize())
        }
        "List" -> MutableList::class.asClassName()
        else -> associatedImports?.let { it[input] } ?: ClassName.bestGuess(input)
      }
    }

    private fun resolveKotlinClassOrInterfaceType(
        inputType: ClassOrInterfaceType,
        methodAnnotations: List<AnnotationExpr>?,
        associatedImports: Map<String, ClassName>? = null): TypeName {
      return if (isObservableObject(inputType)) {
        UNIT_OBSERVABLE
      } else {
        val typeArgs = resolveTypeArguments(inputType, methodAnnotations, associatedImports)
        val rawType = resolveKotlinTypeByName(inputType.name, associatedImports)
        if (typeArgs.isEmpty()) {
          rawType
        } else {
          ParameterizedTypeName.get(rawType, *typeArgs.toTypedArray())
        }
      }
    }

    private fun isObservableObject(inputType: ClassOrInterfaceType): Boolean {
      return inputType.name == "Observable" &&
          inputType.typeArgs?.first() == ReferenceType(ClassOrInterfaceType("Object"))
    }

    private fun resolveTypeArguments(inputType: ClassOrInterfaceType,
        methodAnnotations: List<AnnotationExpr>?,
        associatedImports: Map<String, ClassName>? = null): List<TypeName> {
      return inputType.typeArgs?.map { type: Type ->
        resolveKotlinType(type, methodAnnotations, associatedImports)
      } ?: emptyList()
    }

    private fun resolveKotlinWildcardType(inputType: WildcardType,
        methodAnnotations: List<AnnotationExpr>?,
        associatedImports: Map<String, ClassName>? = null): WildcardTypeName {
      val isNullable = isWildcardNullable(methodAnnotations)
      inputType.`super`?.let { name ->
        val type = WildcardTypeName.supertypeOf(
            resolveKotlinType(name, associatedImports = associatedImports))
        return if (isNullable) type.asNullable() else type
      }
      inputType.extends?.let { name ->
        val type = WildcardTypeName.subtypeOf(
            resolveKotlinType(name, associatedImports = associatedImports))
        return if (isNullable) type.asNullable() else type
      }
      throw IllegalStateException("Wildcard with no super or extends")
    }

    private fun isWildcardNullable(annotations: List<AnnotationExpr>?): Boolean {
      return annotations?.any { it == GenericTypeNullableAnnotation } ?: false
    }
  }

  @TaskAction
  @Suppress("unused")
  fun generate(@Suppress("UNUSED_PARAMETER") inputs: IncrementalTaskInputs) {
    // Clear things out first to make sure no stragglers are left
    val outputDir = File("${project.projectDir}-kotlin${SLASH}src${SLASH}main${SLASH}kotlin")
    outputDir.walkTopDown()
        .filter { it.isFile }
        .filterNot { it.absolutePath.contains("internal") }
        .forEach { it.delete() }

    // Let's get going
    getSource().forEach { generateKotlin(it) }
  }

  private fun generateKotlin(file: File) {
    val outputPath = file.parent.replace("java", "kotlin")
        .replace("${SLASH}src", "-kotlin${SLASH}src")
        .substringBefore("com${SLASH}jakewharton")
    val outputDir = File(outputPath)

    // Start parsing the java files
    val cu = JavaParser.parse(file)

    val kClass = KFile()
    kClass.fileName = file.name.removeSuffix(".java")
    val imports = mutableListOf<String>()

    // Visit the imports first so we can create an associate of them for lookups later.
    cu.accept(object : VoidVisitorAdapter<MutableList<String>>() {

      override fun visit(n: ImportDeclaration, importsList: MutableList<String>) {
        if (!n.isStatic) {
          importsList.add(n.name.toString())
        }
        super.visit(n, importsList)
      }

    }, imports)

    // Create an association of imports simple name -> ClassName
    // This is necessary because JavaParser doesn't yield the fully qualified classname of types, so
    // this is a bit of a trick to just reuse the imports from the target class as a reference for
    // what they're referring to.
    val associatedImports = imports.associateBy({ it.substringAfterLast(".") }) {
      ClassName.bestGuess(it)
    }

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
        arg.methods.add(KMethod(n, associatedImports))
        // Explicitly avoid going deeper, we only care about top level methods. Otherwise
        // we'd hit anonymous inner classes and whatnot
      }

    }, kClass)

    kClass.generate(outputDir)
  }

  /**
   * Represents a kotlin file that corresponds to a Java file/class in an RxBinding module
   */
  class KFile {
    var fileName: String by Delegates.notNull()
    var packageName: String by Delegates.notNull()
    var bindingClass: String by Delegates.notNull()
    var extendedClass: String by Delegates.notNull()
    val methods = mutableListOf<KMethod>()

    /** Generates the code and writes it to the desired directory */
    fun generate(directory: File) {
      KotlinFile.builder(packageName, fileName)
          .apply {
            if (methods.any { it.emitsUnit() }) {
              addStaticImport("com.jakewharton.rxbinding2.internal", "VoidToUnit")
            }
            methods.map { it.generate(ClassName.bestGuess(bindingClass)) }
                .forEach { addFun(it) }
          }
          // @file:Suppress("NOTHING_TO_INLINE")
          .addFileAnnotation(AnnotationSpec.builder(Suppress::class)
              .useSiteTarget(FILE)
              .addMember("names", "%S", "NOTHING_TO_INLINE")
              .build())
          .build()
          .writeTo(directory)

    }
  }

  /**
   * Represents a method implementation that needs to be wired up in Kotlin
   *
   * @param associatedImports a mapping of associated imports by simple name -> [ClassName]. This is
   * necessary because JavaParser doesn't yield the fully qualified class name to look up, but we
   * can snag the imports from the target class ourselves and refer to them as needed.
   */
  class KMethod(n: MethodDeclaration,
      private val associatedImports: Map<String, ClassName>) {
    private val name = n.name
    private val annotations: List<AnnotationExpr> = n.annotations
    private val comment = n.comment?.content?.let { cleanUpDoc(it) }
    private val extendedClass = resolveKotlinType(n.parameters[0].type,
        associatedImports = associatedImports)
    private val parameters = n.parameters.subList(1, n.parameters.size)
    private val returnType = n.type
    private val typeParameters = typeParams(n.typeParameters)
    private val kotlinType = resolveKotlinType(returnType, annotations, associatedImports)

    /** Cleans up the generated doc and translates some html to equivalent markdown for Kotlin docs */
    private fun cleanUpDoc(doc: String): String {
      return doc
          .replace("   * ", "")
          .replace("   *", "")
          .replace("<em>", "*")
          .replace("</em>", "*")
          .replace("<p>", "")
          // {@code view} -> `view`
          .replace("\\{@code ($DOC_LINK_REGEX)}".toRegex()) { result: MatchResult ->
            val codeName = result.destructured
            "`${codeName.component1()}`"
          }
          // {@link Foo} -> [Foo]
          .replace("\\{@link ($DOC_LINK_REGEX)}".toRegex()) { result: MatchResult ->
            val foo = result.destructured
            "[${foo.component1()}]"
          }
          // {@link Foo#bar} -> [Foo.bar]
          .replace(
              "\\{@link ($DOC_LINK_REGEX)#($DOC_LINK_REGEX)}".toRegex()) { result: MatchResult ->
            val (foo, bar) = result.destructured
            "[$foo.$bar]"
          }
          // {@linkplain Foo baz} -> [baz][Foo]
          .replace(
              "\\{@linkplain ($DOC_LINK_REGEX) ($DOC_LINK_REGEX)}".toRegex()) { result: MatchResult ->
            val (foo, baz) = result.destructured
            "[$baz][$foo]"
          }
          //{@linkplain Foo#bar baz} -> [baz][Foo.bar]
          .replace(
              "\\{@linkplain ($DOC_LINK_REGEX)#($DOC_LINK_REGEX) ($DOC_LINK_REGEX)}".toRegex()) { result: MatchResult ->
            val (foo, bar, baz) = result.destructured
            "[$baz][$foo.$bar]"
          }
          .trim()
          .plus("\n")
    }

    /** Generates method level type parameters */
    private fun typeParams(params: List<TypeParameter>?): List<TypeVariableName>? {
      return params?.map { p ->
        TypeVariableName(p.name,
            resolveKotlinType(p.typeBound[0], associatedImports = associatedImports))
      }
    }

    /**
     * Generates parameters in a kotlin-style format
     */
    private fun kParams(): List<ParameterSpec> {
      return parameters.map { p ->
        ParameterSpec.builder(p.id.name,
            resolveKotlinType(p.type, associatedImports = associatedImports))
            .build()
      }
    }

    /**
     * Generates the kotlin code for this method
     *
     * @param bindingClass name of the RxBinding class this is tied to
     */
    fun generate(bindingClass: TypeName): FunSpec {
      ///////////////
      // STRUCTURE //
      ///////////////
      // Javadoc
      // public inline fun DrawerLayout.drawerOpen(): Observable<Boolean> = RxDrawerLayout.drawerOpen(this)
      // <access specifier> inline fun <extendedClass>.<name>(params): <type> = <bindingClass>.name(this, params)

      val parameterSpecs = kParams()
      return FunSpec.builder(name)
          .receiver(extendedClass)
          .addKdoc(comment ?: "")
          .addModifiers(INLINE)
          .apply {
            typeParameters?.let { addTypeVariables(it) }
          }
          .returns(kotlinType)
          .addParameters(parameterSpecs)
          .addCode("return %T.$name(${if (parameterSpecs.isNotEmpty()) {
            "this, ${parameterSpecs.joinToString { it.name }}"
          } else {
            "this"
          }})", bindingClass)
          .apply {
            // Object --> Unit mapping
            if (emitsUnit()) {
              addCode(".map(VoidToUnit)")
            }
          }
          .addCode("\n")
          .build()
    }

    fun emitsUnit() = kotlinType == UNIT_OBSERVABLE

  }
}
