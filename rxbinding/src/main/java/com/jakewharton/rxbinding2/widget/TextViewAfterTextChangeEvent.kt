package com.jakewharton.rxbinding2.widget

import android.content.Context
import android.text.Editable
import android.widget.TextView

/**
 * An after text-change event on a view.
 *
 * **Warning:** Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated [Context].
 */
data class TextViewAfterTextChangeEvent(
  /** The view from which this event occurred.  */
  val view: TextView,
  val editable: Editable?
)
