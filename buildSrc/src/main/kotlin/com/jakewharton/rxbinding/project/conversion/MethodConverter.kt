package com.jakewharton.rxbinding.project.conversion

import com.github.javaparser.ast.TypeParameter
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.expr.AnnotationExpr
import com.github.javaparser.ast.expr.Expression
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeVariableName


/**
 * Generates the kotlin code for this method
 *
 * @param bindingClassName name of the RxBinding class this is tied to
 */
fun MethodDeclaration.toFunSpec(
  associatedImports: Map<String, ClassName>,
  bindingClassName: String,
  requiresApi: AnnotationExpr?
): FunSpec {
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
      .addAnnotations(getAnnotationSpecs(requiresApi))
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

private fun MethodDeclaration.getAnnotationSpecs(requiresApi: AnnotationExpr?): List<AnnotationSpec> {
  val annotations = annotations
      .filterNot { it.name.toString() == "NonNull" }
      .filterNot { it == GenericTypeNullableAnnotation }
      .map { it.annotationSpec(this) }
  if (annotations.none { it.type == REQUIRES_API } && requiresApi != null) {
    return annotations + requiresApiAnnotationSpec(requiresApi)
  }
  return annotations
}

private fun AnnotationExpr.annotationSpec(method: MethodDeclaration) = when (name.toString()) {
  "CheckResult" -> checkResultAnnotationSpec()
  "RequiresApi" -> requiresApiAnnotationSpec(this)
  "Deprecated" -> deprecatedAnnotationSpec(method)
  else -> throw UnsupportedOperationException("No logic for $name annotation")
}

private fun checkResultAnnotationSpec() = AnnotationSpec.builder(CHECK_RESULT).build()

private fun requiresApiAnnotationSpec(expression: Expression) = AnnotationSpec.builder(REQUIRES_API)
    .addMember("%L", (expression as SingleMemberAnnotationExpr).memberValue)
    .build()

private fun deprecatedAnnotationSpec(method: MethodDeclaration): AnnotationSpec {
  val comment = method.comment.content
  val message = "@deprecated ([^.]+\\.)".toRegex().findAll(comment).single().groups[1]!!.value
  return AnnotationSpec.builder(Deprecated::class.java)
      .addMember("%S", message)
      .build()
}
