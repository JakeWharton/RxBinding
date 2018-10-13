package com.jakewharton.rxbinding2.widget

import android.widget.RatingBar

data class RatingBarChangeEvent(
  /** The view from which this event occurred.  */
  val view: RatingBar,
  val rating: Float,
  val fromUser: Boolean
)
