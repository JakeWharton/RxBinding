package com.jakewharton.rxbinding2.support.v7.widget

import android.support.v7.widget.SearchView

data class SearchViewQueryTextEvent(
  /** The view from which this event occurred.  */
  val view: SearchView,
  val queryText: CharSequence,
  val isSubmitted: Boolean
)
