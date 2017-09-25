package com.jakewharton.rxbinding.project

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.ImportDeclaration
import com.github.javaparser.ast.PackageDeclaration
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.type.ClassOrInterfaceType
import com.github.javaparser.ast.type.ReferenceType
import com.github.javaparser.ast.type.Type
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KotlinFile
import java.io.File
import kotlin.properties.Delegates

fun File.convertToKotlinFile(): KotlinFile {
  val javaFile = JavaParser.parse(this)
  val packageName = getPackageName(javaFile)
  val associatedImports = getImports(javaFile)
  val bindingClass = getBindingClass(javaFile)
  val methods = getMethods(javaFile)
  val funSpecs = methods.map { it.toFunSpec(associatedImports, bindingClass) }
  return KotlinFile.builder(packageName, name.removeSuffix(".java"))
      .addVoidToUnitImport(methods)
      .addFunSpecs(funSpecs)
      .suppressNotingToInline()
      .build()
}

private fun KotlinFile.Builder.addVoidToUnitImport(methods: List<MethodDeclaration>) = apply {
  if (methods.any { it.emitsUnit() }) {
    addStaticImport("com.jakewharton.rxbinding2.internal", "VoidToUnit")
  }
}

private fun KotlinFile.Builder.addFunSpecs(funSpecs: List<FunSpec>) = apply {
  funSpecs.forEach { addFun(it) }
}

private fun KotlinFile.Builder.suppressNotingToInline(): KotlinFile.Builder {
  // @file:Suppress("NOTHING_TO_INLINE")
  return addFileAnnotation(AnnotationSpec.builder(Suppress::class)
      .useSiteTarget(AnnotationSpec.UseSiteTarget.FILE)
      .addMember("names", "%S", "NOTHING_TO_INLINE")
      .build())
}

private fun MethodDeclaration.emitsUnit(): Boolean {
  val returnType = this.type
  return isObservableUnit(returnType)
}

private fun isObservableUnit(returnType: Type?): Boolean {
  return returnType is ClassOrInterfaceType
      && returnType.name == "Observable"
      && returnType.typeArgs?.first() == ReferenceType(ClassOrInterfaceType("Object"))
      || returnType is ReferenceType && isObservableUnit(returnType.type)
}

private fun getMethods(javaFile: CompilationUnit): List<MethodDeclaration> {
  val methods: ArrayList<MethodDeclaration> = arrayListOf()
  javaFile.accept(object : VoidVisitorAdapter<Unit>() {
    override fun visit(n: MethodDeclaration, arg: Unit) {
      methods.add(n)
      // Explicitly avoid going deeper, we only care about top level methods. Otherwise
      // we'd hit anonymous inner classes and whatnot
    }
  }, Unit)
  return methods
}

private fun getBindingClass(javaFile: CompilationUnit): String {
  var bindingClass: String by Delegates.notNull()
  // Visit the appropriate nodes and extract information
  javaFile.accept(object : VoidVisitorAdapter<Unit>() {

    override fun visit(n: ClassOrInterfaceDeclaration, arg: Unit) {
      bindingClass = n.name
      super.visit(n, arg)
    }
  }, Unit)
  return bindingClass
}

private fun getImports(javaFile: CompilationUnit): Map<String, ClassName> {
  val imports = mutableMapOf<String, ClassName>()

  // Visit the imports first so we can create an associate of them for lookups later.

  // Create an association of imports simple name -> ClassName
  // This is necessary because JavaParser doesn't yield the fully qualified classname of types, so
  // this is a bit of a trick to just reuse the imports from the target class as a reference for
  // what they're referring to.
  javaFile.accept(object : VoidVisitorAdapter<MutableMap<String, ClassName>>() {
    override fun visit(n: ImportDeclaration, importsList: MutableMap<String, ClassName>) {
      if (!n.isStatic) {
        importsList.put(n.name.toString().substringAfterLast("."), ClassName.bestGuess(n.name.toString()))
      }
      super.visit(n, importsList)
    }

  }, imports)
  return imports
}

private fun getPackageName(javaFile: CompilationUnit): String {
  var packageName by Delegates.notNull<String>()
  javaFile.accept(object : VoidVisitorAdapter<Unit>() {

    override fun visit(n: PackageDeclaration, arg: Unit) {
      packageName = n.name.toString()
      super.visit(n, arg)
    }
  }, Unit)
  return packageName
}
