package com.jakewharton.rxbinding.project

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
import com.squareup.kotlinpoet.*
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs
import java.io.File
import kotlin.properties.Delegates

private val SLASH = File.separator
val UNIT_OBSERVABLE = ParameterizedTypeName.get(
    ClassName("io.reactivex", "Observable"), UNIT)

open class KotlinGenTask : SourceTask() {

  @TaskAction
  @Suppress("unused")
  fun generate(@Suppress("UNUSED_PARAMETER") inputs: IncrementalTaskInputs) {
    removeOldFiles()
    convertSource()
  }

  private fun removeOldFiles() {
    File("${project.projectDir}-kotlin${SLASH}src${SLASH}main${SLASH}kotlin")
        .walkTopDown()
        .filter { it.isFile }
        .filterNot { it.absolutePath.contains("internal") }
        .forEach { it.delete() }
  }

  private fun convertSource() {
    getSource().forEach {
      it.convertToKotlinFile()
          .writeTo(it.getKotlinOutputDir())
    }
  }

  private fun File.getKotlinOutputDir() =
      File(parent.replace("java", "kotlin")
          .replace("${SLASH}src", "-kotlin${SLASH}src")
          .substringBefore("com${SLASH}jakewharton"))
}
