package com.jakewharton.rxbinding.project.conversion

import com.github.javaparser.ast.body.MethodDeclaration

private val DOC_LINK_REGEX = "[0-9A-Za-z._]*"

/** Cleans up the generated doc and translates some html to equivalent markdown for Kotlin docs */
val MethodDeclaration.cleanedDocumentation
  get() = comment?.content?.let { cleanUpDoc(it) } ?: ""

private fun cleanUpDoc(doc: String): String {
  return doc
      .replace("   * ", "")
      .replace("   *", "")
      .replace("<em>", "*")
      .replace("</em>", "*")
      .replace("<p>", "")
      // {@code view} -> `view`
      .replace("\\{@code ($DOC_LINK_REGEX)}".toRegex()) { result: MatchResult ->
        val codeName = result.destructured
        "`${codeName.component1()}`"
      }
      // {@link Foo} -> [Foo]
      .replace("\\{@link ($DOC_LINK_REGEX)}".toRegex()) { result: MatchResult ->
        val foo = result.destructured
        "[${foo.component1()}]"
      }
      // {@link Foo#bar} -> [Foo.bar]
      .replace(
          "\\{@link ($DOC_LINK_REGEX)#($DOC_LINK_REGEX)}".toRegex()) { result: MatchResult ->
        val (foo, bar) = result.destructured
        "[$foo.$bar]"
      }
      // {@linkplain Foo baz} -> [baz][Foo]
      .replace(
          "\\{@linkplain ($DOC_LINK_REGEX) ($DOC_LINK_REGEX)}".toRegex()) { result: MatchResult ->
        val (foo, baz) = result.destructured
        "[$baz][$foo]"
      }
      //{@linkplain Foo#bar baz} -> [baz][Foo.bar]
      .replace(
          "\\{@linkplain ($DOC_LINK_REGEX)#($DOC_LINK_REGEX) ($DOC_LINK_REGEX)}".toRegex()) { result: MatchResult ->
        val (foo, bar, baz) = result.destructured
        "[$baz][$foo.$bar]"
      }
      .trim()
      .plus("\n")
}