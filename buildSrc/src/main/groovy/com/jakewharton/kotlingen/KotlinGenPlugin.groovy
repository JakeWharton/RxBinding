package com.jakewharton.kotlingen

import com.android.build.gradle.api.LibraryVariant
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Plugin for generating kotlin bindings for Rx*.java implementations.
 *
 * For every project that applies this plugin, a corresponding "generateKotlinFor" task is created
 * that you can run.
 **/
class KotlinGenPlugin implements Plugin<Project> {

  @Override
  void apply(Project project) {
    project.afterEvaluate {

      // Grab the release variant
      // Convenience approach so that we can grab the source sets off of it
      Collection<LibraryVariant> variants = project.android.libraryVariants
      LibraryVariant variant = variants.find { v -> v.name == "release" }

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
