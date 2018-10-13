package com.jakewharton.rxbinding2.support.v4.view;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import android.view.MenuItem;
import com.jakewharton.rxbinding2.view.MenuItemActionViewEvent;
import com.jakewharton.rxbinding2.view.RxMenuItem;
import io.reactivex.Observable;
import io.reactivex.functions.Predicate;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkNotNull;

/** @deprecated Use {@link RxMenuItem}. */
@Deprecated
public final class RxMenuItemCompat {

  /** @deprecated Use {@link RxMenuItem#actionViewEvents(MenuItem)}. */
  @Deprecated
  @CheckResult @NonNull public static Observable<MenuItemActionViewEvent> actionViewEvents(
      @NonNull MenuItem menuItem) {
    checkNotNull(menuItem, "menuItem == null");
    return RxMenuItem.actionViewEvents(menuItem);
  }

  /** @deprecated Use {@link RxMenuItem#actionViewEvents(MenuItem, Predicate)}. */
  @Deprecated
  @CheckResult @NonNull public static Observable<MenuItemActionViewEvent> actionViewEvents(
      @NonNull MenuItem menuItem, @NonNull Predicate<? super MenuItemActionViewEvent> handled) {
    checkNotNull(menuItem, "menuItem == null");
    checkNotNull(handled, "handled == null");
    return RxMenuItem.actionViewEvents(menuItem, handled);
  }

  private RxMenuItemCompat() {
    throw new AssertionError("No instances.");
  }
}
