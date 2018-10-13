package com.jakewharton.rxbinding2.widget

import android.view.View
import android.widget.AdapterView

data class AdapterViewItemLongClickEvent(
  /** The view from which this event occurred.  */
  val view: AdapterView<*>,
  val clickedView: View,
  val position: Int,
  val id: Long
)
