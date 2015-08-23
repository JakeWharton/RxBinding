package com.jakewharton.rxbindingproject

import com.android.build.gradle.api.LibraryVariant
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Top-level plugin for managing tasks for project modules
 **/
class RxBindingPlugin implements Plugin<Project> {

  @Override
  void apply(Project project) {
    project.afterEvaluate {

      // Grab the release variant
      // Convenience approach so that we can grab the source sets off of it
      Collection<LibraryVariant> variants = project.android.libraryVariants
      LibraryVariant variant = variants.find { v -> v.name == "release" }

      // Create a "generateKotlinFor" task for generating kotlin bindings
      KotlinGenTask genTask = project.task(type: KotlinGenTask, "generateKotlin") {
        source = variant.getSourceSets().collect { it.getJavaDirectories() }
        include "**/Rx*.java"
        exclude "**/internal/*"
      } as KotlinGenTask

      genTask.outputs.upToDateWhen { false }
      project.tasks.add(genTask)
    }
  }

}
