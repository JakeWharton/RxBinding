package com.jakewharton.rxbinding3.widget

import android.view.View
import android.widget.AdapterView

sealed class AdapterViewSelectionEvent {
  /** The view from which this event occurred.  */
  abstract val view: AdapterView<*>
}

data class AdapterViewItemSelectionEvent(
  override val view: AdapterView<*>,
  val selectedView: View?,
  val position: Int,
  val id: Long
) : AdapterViewSelectionEvent()

data class AdapterViewNothingSelectionEvent(
  override val view: AdapterView<*>
) : AdapterViewSelectionEvent()
