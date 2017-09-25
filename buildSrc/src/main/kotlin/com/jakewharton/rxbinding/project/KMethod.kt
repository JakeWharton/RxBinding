package com.jakewharton.rxbinding.project

import com.github.javaparser.ast.TypeParameter
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.expr.AnnotationExpr
import com.squareup.kotlinpoet.*

/**
 * Represents a method implementation that needs to be wired up in Kotlin
 *
 * @param associatedImports a mapping of associated imports by simple name -> [ClassName]. This is
 * necessary because JavaParser doesn't yield the fully qualified class name to look up, but we
 * can snag the imports from the target class ourselves and refer to them as needed.
 */
class KMethod(n: MethodDeclaration,
              private val associatedImports: Map<String, ClassName>) {
  val name = n.name
  val annotations: List<AnnotationExpr> = n.annotations
  val comment = n.comment?.content?.let { cleanUpDoc(it) }
  val extendedClass = KotlinTypeResolver.resolveKotlinType(n.parameters[0].type,
      associatedImports = associatedImports)
  val parameters = n.parameters.subList(1, n.parameters.size)
  val returnType = n.type
  val typeParameters = typeParams(n.typeParameters)
  val kotlinType = KotlinTypeResolver.resolveKotlinType(returnType, annotations, associatedImports)

  /** Cleans up the generated doc and translates some html to equivalent markdown for Kotlin docs */
  private fun cleanUpDoc(doc: String): String {
    return doc
        .replace("   * ", "")
        .replace("   *", "")
        .replace("<em>", "*")
        .replace("</em>", "*")
        .replace("<p>", "")
        // {@code view} -> `view`
        .replace("\\{@code (${KotlinGenTask.DOC_LINK_REGEX})}".toRegex()) { result: MatchResult ->
          val codeName = result.destructured
          "`${codeName.component1()}`"
        }
        // {@link Foo} -> [Foo]
        .replace("\\{@link (${KotlinGenTask.DOC_LINK_REGEX})}".toRegex()) { result: MatchResult ->
          val foo = result.destructured
          "[${foo.component1()}]"
        }
        // {@link Foo#bar} -> [Foo.bar]
        .replace(
            "\\{@link (${KotlinGenTask.DOC_LINK_REGEX})#(${KotlinGenTask.DOC_LINK_REGEX})}".toRegex()) { result: MatchResult ->
          val (foo, bar) = result.destructured
          "[$foo.$bar]"
        }
        // {@linkplain Foo baz} -> [baz][Foo]
        .replace(
            "\\{@linkplain (${KotlinGenTask.DOC_LINK_REGEX}) (${KotlinGenTask.DOC_LINK_REGEX})}".toRegex()) { result: MatchResult ->
          val (foo, baz) = result.destructured
          "[$baz][$foo]"
        }
        //{@linkplain Foo#bar baz} -> [baz][Foo.bar]
        .replace(
            "\\{@linkplain (${KotlinGenTask.DOC_LINK_REGEX})#(${KotlinGenTask.DOC_LINK_REGEX}) (${KotlinGenTask.DOC_LINK_REGEX})}".toRegex()) { result: MatchResult ->
          val (foo, bar, baz) = result.destructured
          "[$baz][$foo.$bar]"
        }
        .trim()
        .plus("\n")
  }

  /** Generates method level type parameters */
  private fun typeParams(params: List<TypeParameter>?): List<TypeVariableName>? {
    return params?.map { p ->
      TypeVariableName(p.name,
          KotlinTypeResolver.resolveKotlinType(p.typeBound[0], associatedImports = associatedImports))
    }
  }

  /**
   * Generates parameters in a kotlin-style format
   */
  fun kParams(): List<ParameterSpec> {
    return parameters.map { p ->
      ParameterSpec.builder(p.id.name,
          KotlinTypeResolver.resolveKotlinType(p.type, associatedImports = associatedImports))
          .build()
    }
  }

  fun emitsUnit() = kotlinType == UNIT_OBSERVABLE

}