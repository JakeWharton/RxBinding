package com.jakewharton.kotlingen

import com.android.build.gradle.api.LibraryVariant
import com.android.builder.model.SourceProvider
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileTree
import rx.Observable

/**
 * Plugin for generating kotlin bindings for Rx*.java implementations.
 *
 * For every project that applies this plugin, a corresponding "generateKotlinFor<project name>" task
 * is created that you can run.
 *
 * Example: applying this in the rxbinding module results in a task called "generateKotlinForRxbinding"
 **/
class KotlinGenPlugin implements Plugin<Project> {

  @Override
  void apply(Project project) {
    project.afterEvaluate {

      // Grab the release variant
      // Convenience approach so that we can grab the source sets off of it
      Collection<LibraryVariant> variants = project.android.libraryVariants
      LibraryVariant variant = variants.find { v -> v.name == "release" }

      KotlinGenTask genTask = project.task(type: KotlinGenTask, "generateKotlinFor${project.name.capitalize()}") {
        source = getFilesFromSourceSets(project, variant.getSourceSets()).toBlocking().first()
      } as KotlinGenTask

      genTask.outputs.upToDateWhen { false }
      project.tasks.add(genTask)
    }
  }

  static Observable<FileTree> getFilesFromSourceSets(Project project, List<SourceProvider> sourceSets) {
    return Observable.from(sourceSets)
        .flatMap({ sourceSet -> Observable.from(sourceSet.getJavaDirectories()) })
        .map({ source -> project.fileTree(dir: source, include: "**/Rx*.java", exclude: "**/internal/*") })
        .reduce({ FileTree cur, FileTree next -> cur + next })
  }

}