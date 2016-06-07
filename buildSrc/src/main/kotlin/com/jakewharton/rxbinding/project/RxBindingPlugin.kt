package com.jakewharton.rxbinding.project

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class RxBindingPlugin : Plugin<Project> {

  companion object {
    private val INCLUDE_PATTERN = "**/Rx*.java"
    private val EXCLUDE_PATTERN = "**/internal/*"
  }

  override fun apply(project: Project) {
    project.afterEvaluate {

      // Grab the release variant
      // Convenience approach so that we can grab the source sets off of it

      val variants = project.extensions.getByType(LibraryExtension::class.java).libraryVariants
      val variant = variants.first { it.name == "release" }
      val variantJavaSources = variant.sourceSets.map { it.javaDirectories }

      // Create a "generateKotlinFor" task for generating kotlin bindings
      val genTask = project.tasks.create("generateKotlin", KotlinGenTask::class.java).apply {
        source(variantJavaSources)
        include(INCLUDE_PATTERN)
        exclude(EXCLUDE_PATTERN)
      }

      genTask.outputs.upToDateWhen { false }
      project.tasks.add(genTask)

      // Task for validating Rx*.java factory method structures
      val validateBindingsTask
          = project.tasks.create("validateBindings", ValidateBindingsTask::class.java).apply {
        source(variantJavaSources)
        include(INCLUDE_PATTERN)
        exclude(EXCLUDE_PATTERN)
      }

      project.tasks.findByName("check").dependsOn(validateBindingsTask)
    }
  }
}
