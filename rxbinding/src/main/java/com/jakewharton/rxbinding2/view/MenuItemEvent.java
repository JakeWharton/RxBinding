package com.jakewharton.rxbinding2.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.MenuItem;

/**
 * A target menu item on which an event occurred (e.g., click).
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the menu item. Operators that
 * cache instances have the potential to leak the associated {@link Context}.
 */
public abstract class MenuItemEvent<T extends MenuItem> {
  private final T menuItem;

  protected MenuItemEvent(@NonNull T menuItem) {
    this.menuItem = menuItem;
  }

  /** The menu item from which this event occurred. */
  public @NonNull T menuItem() {
    return menuItem;
  }
}
