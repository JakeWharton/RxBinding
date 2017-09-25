package com.jakewharton.rxbinding.project

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.KotlinFile
import java.io.File
import kotlin.properties.Delegates

/**
 * Represents a kotlin file that corresponds to a Java file/class in an RxBinding module
 */
class KFile {
  var fileName: String by Delegates.notNull()
  var packageName: String by Delegates.notNull()
  var bindingClass: String by Delegates.notNull()
  var extendedClass: String by Delegates.notNull()
  val methods = mutableListOf<KMethod>()


}