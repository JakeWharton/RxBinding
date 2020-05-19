package com.jakewharton.rxbinding4.view

import android.content.Context
import android.view.MenuItem

/**
 * An action view event on a menu item.
 *
 * **Warning:** Instances keep a strong reference to the menu item. Operators that
 * cache instances have the potential to leak the associated [Context].
 */
sealed class MenuItemActionViewEvent {
  /** The menu item from which this event occurred.  */
  abstract val menuItem: MenuItem
}

data class MenuItemActionViewCollapseEvent(
  override val menuItem: MenuItem
) : MenuItemActionViewEvent()

data class MenuItemActionViewExpandEvent(
  override val menuItem: MenuItem
) : MenuItemActionViewEvent()
