package com.jakewharton.rxbinding.project.conversion

import com.github.javaparser.ast.expr.AnnotationExpr
import com.github.javaparser.ast.expr.MarkerAnnotationExpr
import com.github.javaparser.ast.expr.NameExpr
import com.github.javaparser.ast.type.*
import com.jakewharton.rxbinding.project.UNIT_OBSERVABLE
import com.squareup.kotlinpoet.*
import sun.reflect.generics.reflectiveObjects.NotImplementedException

private val GenericTypeNullableAnnotation = MarkerAnnotationExpr(
    NameExpr("GenericTypeNullable"))

/** Recursive function for resolving a Type into a Kotlin-friendly String representation */
fun Type.resolveKotlinType(
    methodAnnotations: List<AnnotationExpr>? = null,
    associatedImports: Map<String, ClassName>? = null): TypeName {
  return when (this) {
    is ReferenceType -> this@resolveKotlinType.type.resolveKotlinType(methodAnnotations, associatedImports)
    is PrimitiveType -> resolveKotlinTypeByName(toString(), associatedImports)
    is VoidType -> resolveKotlinTypeByName(toString(), associatedImports)
    is ClassOrInterfaceType -> resolveKotlinClassOrInterfaceType(this, methodAnnotations,
        associatedImports)
    is WildcardType -> resolveKotlinWildcardType(this, methodAnnotations,
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
    type.resolveKotlinType(methodAnnotations, associatedImports)
  } ?: emptyList()
}

private fun resolveKotlinWildcardType(inputType: WildcardType,
                                      methodAnnotations: List<AnnotationExpr>?,
                                      associatedImports: Map<String, ClassName>? = null): WildcardTypeName {
  val isNullable = isWildcardNullable(methodAnnotations)
  inputType.`super`?.let { name ->
    val type = WildcardTypeName.supertypeOf(
        name.resolveKotlinType(associatedImports = associatedImports))
    return if (isNullable) type.asNullable() else type
  }
  inputType.extends?.let { name ->
    val type = WildcardTypeName.subtypeOf(
        name.resolveKotlinType(associatedImports = associatedImports))
    return if (isNullable) type.asNullable() else type
  }
  throw IllegalStateException("Wildcard with no super or extends")
}

private fun isWildcardNullable(annotations: List<AnnotationExpr>?): Boolean {
  return annotations?.any { it == GenericTypeNullableAnnotation } ?: false
}