package com.jakewharton.rxbinding.project.conversion

import com.github.javaparser.ast.TypeParameter
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.expr.AnnotationExpr
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr
import com.squareup.kotlinpoet.*


/**
 * Generates the kotlin code for this method
 *
 * @param bindingClass name of the RxBinding class this is tied to
 */
fun MethodDeclaration.toFunSpec(associatedImports: Map<String, ClassName>, bindingClassName: String): FunSpec {
  ///////////////
  // STRUCTURE //
  ///////////////
  // Javadoc
  // public inline fun DrawerLayout.drawerOpen(): Observable<Boolean> = RxDrawerLayout.drawerOpen(this)
  // <access specifier> inline fun <extendedClass>.<name>(params): <type> = <bindingClass>.name(this, params)
  val parameterSpecs = paramsSpec(associatedImports)
  return FunSpec.builder(name)
      .addKdoc(cleanedDocumentation)
      .addModifiers(KModifier.INLINE)
      .addAnnotations(getAnnotationSpecs())
      .addTypeVariables(getTypeVariables(associatedImports))
      .returns(type.resolveKotlinType(annotations, associatedImports))
      .receiver(parameters[0].type.resolveKotlinType(associatedImports = associatedImports))
      .addParameters(parameterSpecs)
      .addCode("return %T.$name(${if (parameterSpecs.isNotEmpty()) {
        "this, ${parameterSpecs.joinToString { it.name }}"
      } else {
        "this"
      }})", ClassName.bestGuess(bindingClassName))
      .apply {
        // Object --> Unit mapping
        if (emitsUnit()) {
          addCode(".map(VoidToUnit)")
        }
      }
      .addCode("\n")
      .build()
}

/**
 * Generates parameters in a kotlin-style format
 */
private fun MethodDeclaration.paramsSpec(associatedImports: Map<String, ClassName>): List<ParameterSpec> {
  return parameters.drop(1).map { p ->
    ParameterSpec.builder(p.id.name,
        p.type.resolveKotlinType(associatedImports = associatedImports))
        .build()
  }
}

private fun MethodDeclaration.getTypeVariables(associatedImports: Map<String, ClassName>) =
    typeParameters.map { it.typeParams(associatedImports) }


/** Generates method level type parameters */
private fun TypeParameter.typeParams(associatedImports: Map<String, ClassName>): TypeVariableName {
  val typeName = typeBound[0].resolveKotlinType(associatedImports = associatedImports)
  return TypeVariableName(name, typeName)
}

private fun MethodDeclaration.getAnnotationSpecs(): Iterable<AnnotationSpec> {
  return annotations
      .filterNot { it.name.toString() == "NonNull" }
      .filterNot { it == GenericTypeNullableAnnotation }
      .map { it.annotationSpec(this) }
}

private fun AnnotationExpr.annotationSpec(method: MethodDeclaration) = when (name.toString()) {
  "CheckResult" -> checkResultAnnotationSpec()
  "RequiresApi" -> requiresApiAnnotationSpec()
  "Deprecated" -> deprecatedAnnotationSpec(method)
  else -> throw UnsupportedOperationException("No logic for $name annotation")
}

private fun checkResultAnnotationSpec() =
    AnnotationSpec.builder(ClassName("android.support.annotation", "CheckResult")).build()

private fun AnnotationExpr.requiresApiAnnotationSpec() =
    AnnotationSpec.builder(ClassName("android.support.annotation", "RequiresApi"))
        .addMember("%L", apiVersion()).build()

private fun deprecatedAnnotationSpec(method: MethodDeclaration): AnnotationSpec {
  val comment = method.comment.content
  val message = "@deprecated ([^.]+\\.)".toRegex().findAll(comment).single().groups[1]!!.value
  return AnnotationSpec.builder(Deprecated::class.java)
      .addMember("%S", message)
      .build()
}

private fun AnnotationExpr.apiVersion() = "android.os.Build.VERSION_CODES."+(this as SingleMemberAnnotationExpr).memberValue
