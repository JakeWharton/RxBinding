package com.jakewharton.rxbinding4.leanback

import androidx.leanback.widget.SearchBar

sealed class SearchBarSearchQueryEvent {
  /** The view from which this event occurred.  */
  abstract val view: SearchBar
  abstract val searchQuery: String
}

data class SearchBarSearchQueryChangedEvent(
  override val view: SearchBar,
  override val searchQuery: String
) : SearchBarSearchQueryEvent()

data class SearchBarSearchQueryKeyboardDismissedEvent(
  override val view: SearchBar,
  override val searchQuery: String
) : SearchBarSearchQueryEvent()

data class SearchBarSearchQuerySubmittedEvent(
  override val view: SearchBar,
  override val searchQuery: String
) : SearchBarSearchQueryEvent()
