package com.jakewharton.rxbinding2.view;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.MenuItem;

/**
 * An action view event on a menu item.
 * <p>
 * <strong>Warning:</strong> Instances keep a strong reference to the menu item. Operators that
 * cache instances have the potential to leak the associated {@link Context}.
 */
public final class MenuItemActionViewEvent extends MenuItemEvent<MenuItem> {
  public enum Kind {
    EXPAND, COLLAPSE
  }

  @CheckResult @NonNull
  public static MenuItemActionViewEvent create(@NonNull MenuItem menuItem, @NonNull Kind kind) {
    return new MenuItemActionViewEvent(menuItem, kind);
  }

  private final Kind kind;

  private MenuItemActionViewEvent(@NonNull MenuItem menuItem, @NonNull Kind kind) {
    super(menuItem);
    this.kind = kind;
  }

  @NonNull
  public Kind kind() {
    return kind;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MenuItemActionViewEvent that = (MenuItemActionViewEvent) o;

    if (!menuItem().equals(that.menuItem())) return false;
    return kind == that.kind;
  }

  @Override public int hashCode() {
    int result = menuItem().hashCode();
    result = 31 * result + kind.hashCode();
    return result;
  }

  @Override public String toString() {
    return "MenuItemActionViewEvent{"
        + "menuItem="
        + menuItem()
        + ", kind="
        + kind
        + '}';
  }
}
