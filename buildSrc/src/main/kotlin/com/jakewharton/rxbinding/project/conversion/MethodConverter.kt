package com.jakewharton.rxbinding.project.conversion

import com.github.javaparser.ast.TypeParameter
import com.github.javaparser.ast.body.MethodDeclaration
import com.jakewharton.rxbinding.project.KotlinTypeResolver
import com.squareup.kotlinpoet.*


/**
 * Generates the kotlin code for this method
 *
 * @param bindingClass name of the RxBinding class this is tied to
 */
fun MethodDeclaration.toFunSpec(associatedImports: Map<String, ClassName>, bindingClassName: String): FunSpec {
  val m = this

  ///////////////
  // STRUCTURE //
  ///////////////
  // Javadoc
  // public inline fun DrawerLayout.drawerOpen(): Observable<Boolean> = RxDrawerLayout.drawerOpen(this)
  // <access specifier> inline fun <extendedClass>.<name>(params): <type> = <bindingClass>.name(this, params)
  val parameterSpecs = paramsSpec(m, associatedImports)
  return FunSpec.builder(m.name)
      .receiver(KotlinTypeResolver.resolveKotlinType(m.parameters[0].type, associatedImports = associatedImports))
      .addKdoc(m.comment?.content?.let { cleanUpDoc(it) } ?: "")
      .addModifiers(KModifier.INLINE)
      .addMultipleTypeVariables(m, associatedImports)
      .returns(KotlinTypeResolver.resolveKotlinType(m.type, m.annotations, associatedImports))
      .addParameters(parameterSpecs)
      .addCode("return %T.$name(${if (parameterSpecs.isNotEmpty()) {
        "this, ${parameterSpecs.joinToString { it.name }}"
      } else {
        "this"
      }})", ClassName.bestGuess(bindingClassName))
      .apply {
        // Object --> Unit mapping
        if (m.emitsUnit()) {
          addCode(".map(VoidToUnit)")
        }
      }
      .addCode("\n")
      .build()
}


/**
 * Generates parameters in a kotlin-style format
 */
private fun paramsSpec(m: MethodDeclaration, associatedImports: Map<String, ClassName>): List<ParameterSpec> {
  return m.parameters.subList(1, m.parameters.size).map { p ->
    ParameterSpec.builder(p.id.name,
        KotlinTypeResolver.resolveKotlinType(p.type, associatedImports = associatedImports))
        .build()
  }
}

private fun FunSpec.Builder.addMultipleTypeVariables(m: MethodDeclaration, associatedImports: Map<String, ClassName>) = apply {
  m.typeParameters.map { it.typeParams(associatedImports) }.let { addTypeVariables(it) }
}

/** Generates method level type parameters */
private fun TypeParameter.typeParams(associatedImports: Map<String, ClassName>): TypeVariableName {
  val typeName = KotlinTypeResolver.resolveKotlinType(typeBound[0], associatedImports = associatedImports)
  return TypeVariableName(name, typeName)
}
