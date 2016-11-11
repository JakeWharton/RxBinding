package com.jakewharton.rxbinding.project

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.type.ClassOrInterfaceType
import com.github.javaparser.ast.type.ReferenceType
import com.github.javaparser.ast.type.WildcardType
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs
import java.io.File

open class ValidateBindingsTask : SourceTask() {

  private val METHOD_ANNOTATIONS = listOf(
      "CheckResult",
      "NonNull"
  )

  @TaskAction
  @Suppress("unused")
  fun validate(inputs: IncrementalTaskInputs) {
    getSource().forEach { verifyJavaFile(it) }
  }

  fun verifyJavaFile(javaFile: File) {
    val cu = JavaParser.parse(javaFile)

    cu.accept(object : VoidVisitorAdapter<Any>() {

      override fun visit(n: MethodDeclaration, arg: Any?) {
        verifyMethodAnnotations(n)
        verifyParameters(n)
        verifyReturnType(n)
        verifyNullChecks(n)
        // Explicitly avoid going deeper, we only care about top level methods. Otherwise
        // we'd hit anonymous inner classes and whatnot
      }

    }, null)
  }

  /** Validates that method signatures have @CheckResult and @NonNull annotations */
  fun verifyMethodAnnotations(method: MethodDeclaration) {
    val annotationNames = method.annotations.map { it.name.toString() }
    METHOD_ANNOTATIONS.forEach { annotation: String ->
      if (!annotationNames.contains(annotation)) {
        throw IllegalStateException("Missing required @$annotation method annotation on ${(method.getEnclosingClass()).name}#${method.name}")
      }
    }
  }

  /**
   * Validates that:
   * - reference type parameters have @NonNull annotations
   * - Second parameters that are instances of Func1 have a wildcard first parameterized type
   */
  fun verifyParameters(method: MethodDeclaration) {
    val params = method.parameters

    // Validate annotations
    params.forEach { p ->
      if (p.type is ReferenceType) {
        if (p.annotations == null || !p.annotations.map { it.name.toString() }.contains("NonNull")) {
          throw IllegalStateException("Missing required @NonNull annotation on ${method.getEnclosingClass().name}#${method.name} parameter: \"${p.id.name}\"")
        }
      }
    }

    // Validate Func1 params
    if (params.size >= 2 && params[1].type is ReferenceType && params[1].coerceClassType().name == "Func1") {
      val func1Param: Parameter = params[1]
      if (!func1Param.typeHasTypeArgs() || func1Param.coerceClassType().typeArgs[0] !is WildcardType) {
        throw IllegalStateException("Missing wildcard type parameter declaration on ${method.getEnclosingClass().name}#${method.name} Func1 parameter: \"${func1Param.id.name}\"")
      }
    }
  }

  /** Validates that Action1 return types have proper wildcard bounds */
  fun verifyReturnType(method: MethodDeclaration) {
    if (method.returnTypeAsType().name == "Action1") {
      if (!method.returnTypeHasTypeArgs() || method.returnTypeAsType().typeArgs[0] !is WildcardType) {
        throw IllegalStateException("Missing wildcard type parameter declaration on ${method.getEnclosingClass().name}#${method.name}'s Action1 return type")
      }
    }
  }

  /** Validates that reference type parameters have corresponding checkNotNull calls at the beginning of the body */
  fun verifyNullChecks(method: MethodDeclaration) {

    val parameters = method.parameters
    val statements = method.body.stmts

    parameters
        .filter { it.type is ReferenceType }
        .map { it.id.name }
        .zip(statements, { param, stmt -> Pair(param, stmt) })
        .forEach {
          val pName = it.first
          val expected = "checkNotNull($pName, \"$pName == null\");"
          val found = it.second.toString()
          if (!expected.equals(found)) {
            throw IllegalStateException("Missing proper checkNotNull call on parameter "
                + pName
                + " in "
                + prettyMethodSignature(method)
                + "\nExpected:\t" + expected
                + "\nFound:\t" + found
            )
          }
        }
  }

  /** Generates a nice String representation of the method signature (e.g. RxView#clicks(View)) */
  fun prettyMethodSignature(method: MethodDeclaration): String {
    val parameterTypeNames = method.parameters
      .map { it.type.toString() }
      .joinToString()
    return "${(method.getEnclosingClass()).name}#${method.name}($parameterTypeNames)"
  }

  fun Parameter.coerceClassType(): ClassOrInterfaceType {
    return (this.type as ReferenceType).type as ClassOrInterfaceType
  }

  fun Parameter.typeHasTypeArgs(): Boolean {
    val returnType = coerceClassType()
    return returnType.typeArgs?.isNotEmpty() ?: false
  }

  fun MethodDeclaration.getEnclosingClass(): ClassOrInterfaceDeclaration {
    return this.parentNode as ClassOrInterfaceDeclaration
  }

  fun MethodDeclaration.returnTypeAsType(): ClassOrInterfaceType {
    return (this.type as ReferenceType).type as ClassOrInterfaceType
  }

  fun MethodDeclaration.returnTypeHasTypeArgs(): Boolean {
    val returnType = returnTypeAsType()
    return returnType.typeArgs?.isNotEmpty() ?: false
  }
}
