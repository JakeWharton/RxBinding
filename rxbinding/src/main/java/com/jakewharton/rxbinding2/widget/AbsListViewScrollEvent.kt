package com.jakewharton.rxbinding2.widget

import android.widget.AbsListView

data class AbsListViewScrollEvent(
  /** The view from which this event occurred.  */
  val view: AbsListView,
  val scrollState: Int,
  val firstVisibleItem: Int,
  val visibleItemCount: Int,
  val totalItemCount: Int
)
