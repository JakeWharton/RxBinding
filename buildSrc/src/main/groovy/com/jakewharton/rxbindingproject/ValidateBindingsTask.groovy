package com.jakewharton.rxbindingproject

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.type.ReferenceType
import com.github.javaparser.ast.type.WildcardType
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import com.google.common.collect.ImmutableList
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs

class ValidateBindingsTask extends SourceTask {

  private static final List<String> METHOD_ANNOTATIONS = ImmutableList.of(
      "CheckResult",
      "NonNull"
  )

  @TaskAction
  def validate(IncrementalTaskInputs inputs) {
    getSource().each { verifyJavaFile(it) }
  }

  static void verifyJavaFile(File javaFile) {
    CompilationUnit cu = JavaParser.parse(javaFile)

    cu.accept(new VoidVisitorAdapter<Object>() {
      @Override
      void visit(MethodDeclaration n, Object arg) {
        verifyMethodAnnotations(n)
        verifyParameters(n)
        verifyReturnType(n)
        // Explicitly avoid going deeper, we only care about top level methods. Otherwise
        // we'd hit anonymous inner classes and whatnot
      }

    }, null)
  }

  /** Validates that method signatures have @CheckResult and @NonNull annotations */
  static void verifyMethodAnnotations(MethodDeclaration method) {
    List<String> annotationNames = method.annotations.collect {it.name.toString()}
    METHOD_ANNOTATIONS.each { String annotation ->
      if (!annotationNames.contains(annotation)) {
        throw new IllegalStateException("Missing required @$annotation method annotation on $method.parentNode.name#$method.name")
      }
    }
  }

  /**
   * Validates that:
   * - reference type parameters have @NonNull annotations
   * - Second parameters that are instances of Func1 have a wildcard first parameterized type
   */
  static void verifyParameters(MethodDeclaration method) {
    List<Parameter> params = method.parameters

    // Validate annotations
    params.each { Parameter p ->
      if (p.type instanceof ReferenceType) {
        if (!p.annotations.collect {it.name.toString()}.contains("NonNull")) {
          throw new IllegalStateException("Missing required @NonNull annotation on $method.parentNode.name#$method.name parameter: \"$p.id.name\"")
        }
      }
    }

    // Validate Func1 params
    if (params.size() >= 2 && params[1] instanceof ReferenceType && params[1].type.type.name == "Func1") {
      Parameter func1Param = params[1]
      if (!func1Param.type.type.typeArgs || !(func1Param.type.type.typeArgs[0] instanceof WildcardType)) {
        throw new IllegalStateException("Missing wildcard type parameter declaration on $method.parentNode.name#$method.name Func1 parameter: \"${func1Param.id.name}\"")
      }
    }
  }

  /** Validates that Action1 return types have proper wildcard bounds */
  static void verifyReturnType(MethodDeclaration method) {
    if (method.type.type.name == "Action1") {
      if (!method.type.type.typeArgs || !(method.type.type.typeArgs[0] instanceof WildcardType)) {
        throw new IllegalStateException("Missing wildcard type parameter declaration on $method.parentNode.name#$method.name's Action1 return type")
      }
    }
  }
}
