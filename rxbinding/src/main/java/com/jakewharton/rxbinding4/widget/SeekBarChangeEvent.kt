package com.jakewharton.rxbinding4.widget

import android.widget.SeekBar

sealed class SeekBarChangeEvent {
  /** The view from which this event occurred.  */
  abstract val view: SeekBar
}

data class SeekBarProgressChangeEvent(
  override val view: SeekBar,
  val progress: Int,
  val fromUser: Boolean
) : SeekBarChangeEvent()

data class SeekBarStartChangeEvent(
  override val view: SeekBar
) : SeekBarChangeEvent()

data class SeekBarStopChangeEvent(
  override val view: SeekBar
) : SeekBarChangeEvent()
