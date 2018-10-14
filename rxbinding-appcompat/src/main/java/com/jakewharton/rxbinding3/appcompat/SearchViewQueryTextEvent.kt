package com.jakewharton.rxbinding3.appcompat

import androidx.appcompat.widget.SearchView

data class SearchViewQueryTextEvent(
  /** The view from which this event occurred.  */
  val view: SearchView,
  val queryText: CharSequence,
  val isSubmitted: Boolean
)
