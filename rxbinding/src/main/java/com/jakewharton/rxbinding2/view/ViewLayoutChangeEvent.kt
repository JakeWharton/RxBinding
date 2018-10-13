package com.jakewharton.rxbinding2.view

import android.content.Context
import android.view.View

/**
 * A layout-change event on a view.
 *
 * **Warning:** Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated [Context].
 */
data class ViewLayoutChangeEvent(
  /** The view from which this event occurred. */
  val view: View,
  val left: Int,
  val top: Int,
  val right: Int,
  val bottom: Int,
  val oldLeft: Int,
  val oldTop: Int,
  val oldRight: Int,
  val oldBottom: Int
)
