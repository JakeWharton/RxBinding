package com.jakewharton.rxbinding.project

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.ImportDeclaration
import com.github.javaparser.ast.PackageDeclaration
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import com.squareup.kotlinpoet.*
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs
import java.io.File

private val SLASH = File.separator
val UNIT_OBSERVABLE = ParameterizedTypeName.get(
    ClassName("io.reactivex", "Observable"), UNIT)

open class KotlinGenTask : SourceTask() {


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
    val kClass = parseJavaFileToKotlinClass(file)


    kClass.writeTo(outputDir)
  }

  private fun parseJavaFileToKotlinClass(file: File): KotlinFile {
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
    return kClass.run {
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
              .useSiteTarget(AnnotationSpec.UseSiteTarget.FILE)
              .addMember("names", "%S", "NOTHING_TO_INLINE")
              .build())
          .build()
    }
  }

  /**
   * Generates the kotlin code for this method
   *
   * @param bindingClass name of the RxBinding class this is tied to
   */
  fun KMethod.generate(bindingClass: TypeName): FunSpec {
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
        .addModifiers(KModifier.INLINE)
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
}
