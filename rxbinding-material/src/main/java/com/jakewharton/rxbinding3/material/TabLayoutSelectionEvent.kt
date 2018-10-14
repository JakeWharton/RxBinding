package com.jakewharton.rxbinding3.material

import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab

sealed class TabLayoutSelectionEvent {
  /** The view from which this event occurred.  */
  abstract val view: TabLayout
  abstract val tab: Tab
}

data class TabLayoutSelectionSelectedEvent(
  override val view: TabLayout,
  override val tab: Tab
) : TabLayoutSelectionEvent()

data class TabLayoutSelectionReselectedEvent(
  override val view: TabLayout,
  override val tab: Tab
) : TabLayoutSelectionEvent()

data class TabLayoutSelectionUnselectedEvent(
  override val view: TabLayout,
  override val tab: Tab
) : TabLayoutSelectionEvent()
