package com.jakewharton.rxbindingproject

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.type.ReferenceType
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import com.google.common.collect.ImmutableList
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs

class VerificationTask extends SourceTask {

  private static final List<String> METHOD_ANNOTATIONS = ImmutableList.of(
      "CheckResult",
      "NonNull"
  )

  @TaskAction
  def verify(IncrementalTaskInputs inputs) {
    getSource().each { verifyJavaFile(it) }
  }

  static void verifyJavaFile(File javaFile) {
    CompilationUnit cu = JavaParser.parse(javaFile)

    cu.accept(new VoidVisitorAdapter<Object>() {
      @Override
      void visit(MethodDeclaration n, Object arg) {
        verifyMethodAnnotations(n)
        verifyParameters(n)
        // Explicitly avoid going deeper, we only care about top level methods. Otherwise
        // we'd hit anonymous inner classes and whatnot
      }

    }, null)
  }

  /** Verifies that method signatures have @CheckResult and @NonNull annotations */
  static void verifyMethodAnnotations(MethodDeclaration method) {
    List<String> annotationNames = method.annotations.collect {it.name.toString()}
    METHOD_ANNOTATIONS.each { String annotation ->
      if (!annotationNames.contains(annotation)) {
        throw new IllegalStateException("Missing required @$annotation method annotation on $method.parentNode.name#$method.name")
      }
    }
  }

  /** Verifies that reference type parameters have @NonNull annotations */
  static void verifyParameters(MethodDeclaration method) {
    List<Parameter> params = method.parameters
    params.each { Parameter p ->
      if (p.type instanceof ReferenceType) {
        if (!p.annotations.collect {it.name.toString()}.contains("NonNull")) {
          throw new IllegalStateException("Missing required @NonNull annotation on $method.parentNode.name#$method.name parameter: \"$p.id.name\"")
        }
      }
    }
  }
}
