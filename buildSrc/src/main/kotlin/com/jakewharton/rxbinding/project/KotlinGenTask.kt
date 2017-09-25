package com.jakewharton.rxbinding.project

import com.jakewharton.rxbinding.project.conversion.convertToKotlinFile
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.UNIT
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs
import java.io.File

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
