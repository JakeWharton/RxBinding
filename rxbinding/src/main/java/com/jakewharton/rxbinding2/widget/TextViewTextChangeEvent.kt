package com.jakewharton.rxbinding2.widget

import android.content.Context
import android.widget.TextView

/**
 * A text-change event on a view.
 *
 * **Warning:** Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated [Context].
 */
data class TextViewTextChangeEvent(
  /** The view from which this event occurred.  */
  val view: TextView,
  val text: CharSequence,
  val start: Int,
  val before: Int,
  val count: Int
)
