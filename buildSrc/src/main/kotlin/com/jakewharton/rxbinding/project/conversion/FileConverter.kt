package com.jakewharton.rxbinding.project.conversion

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
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import java.io.File
import kotlin.properties.Delegates

fun File.convertToKotlinFile(): FileSpec {
  val javaFile = JavaParser.parse(this)

  val packageName = javaFile.`package`.packageName
  val associatedImports = javaFile.imports
      .filterNot { it.isStatic }
      .map { it.name.toString() }
      .associate { it.substringAfterLast(".") to ClassName.bestGuess(it) }

  val rxClass = javaFile.types.single()
  val bindingClass = rxClass.name

  val methods = rxClass.members.filterIsInstance<MethodDeclaration>()
  val funSpecs = methods.map { it.toFunSpec(associatedImports, bindingClass) }

  return FileSpec.builder(packageName, name.removeSuffix(".java"))
      .addVoidToUnitImport(methods)
      .addFunSpecs(funSpecs)
      .suppressNotingToInline()
      .build()
}

private fun FileSpec.Builder.addVoidToUnitImport(methods: List<MethodDeclaration>) = apply {
  if (methods.any { it.emitsUnit() }) {
    addStaticImport("com.jakewharton.rxbinding2.internal", "VoidToUnit")
  }
}

private fun FileSpec.Builder.addFunSpecs(funSpecs: List<FunSpec>) = apply {
  funSpecs.forEach { addFunction(it) }
}

private fun FileSpec.Builder.suppressNotingToInline() = apply {
  // @file:Suppress("NOTHING_TO_INLINE")
  addAnnotation(AnnotationSpec.builder(Suppress::class)
      .useSiteTarget(AnnotationSpec.UseSiteTarget.FILE)
      .addMember("%S", "NOTHING_TO_INLINE")
      .build())
}

fun MethodDeclaration.emitsUnit(): Boolean {
  val returnType = this.type
  return isObservableUnit(returnType)
}

private fun isObservableUnit(returnType: Type?): Boolean {
  return returnType is ClassOrInterfaceType
      && returnType.name == "Observable"
      && returnType.typeArgs?.first() == ReferenceType(ClassOrInterfaceType("Object"))
      || returnType is ReferenceType && isObservableUnit(returnType.type)
}
