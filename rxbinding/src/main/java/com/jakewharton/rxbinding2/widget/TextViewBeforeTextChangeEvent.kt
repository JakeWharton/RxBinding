package com.jakewharton.rxbinding2.widget

import android.content.Context
import android.widget.TextView

/**
 * A before text-change event on a view.
 *
 * **Warning:** Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated [Context].
 */
data class TextViewBeforeTextChangeEvent(
  /** The view from which this event occurred.  */
  val view: TextView,
  val text: CharSequence,
  val start: Int,
  val count: Int,
  val after: Int
)
