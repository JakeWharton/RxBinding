package com.jakewharton.rxbinding2.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.MenuItem;

/**
 * An action view event on a menu item.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the menu item. Operators that
 * cache instances have the potential to leak the associated {@link Context}.
 */
public abstract class MenuItemActionViewEvent {
  MenuItemActionViewEvent() {
  }

  /** The menu item from which this event occurred. */
  public abstract @NonNull MenuItem menuItem();
}
