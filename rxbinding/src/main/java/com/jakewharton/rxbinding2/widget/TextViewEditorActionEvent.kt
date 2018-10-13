package com.jakewharton.rxbinding2.widget

import android.view.KeyEvent
import android.widget.TextView

data class TextViewEditorActionEvent(
  /** The view from which this event occurred.  */
  val view: TextView,
  val actionId: Int,
  val keyEvent: KeyEvent?
)
