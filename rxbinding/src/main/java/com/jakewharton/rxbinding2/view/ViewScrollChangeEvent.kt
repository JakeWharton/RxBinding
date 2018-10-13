package com.jakewharton.rxbinding2.view

import android.view.View
import androidx.annotation.CheckResult

/**
 * A scroll-change event on a view.
 *
 * **Warning:** Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated [android.content.Context].
 */
data class ViewScrollChangeEvent(
  /** The view from which this event occurred.  */
  val view: View,
  val scrollX: Int,
  val scrollY: Int,
  val oldScrollX: Int,
  val oldScrollY: Int
)
