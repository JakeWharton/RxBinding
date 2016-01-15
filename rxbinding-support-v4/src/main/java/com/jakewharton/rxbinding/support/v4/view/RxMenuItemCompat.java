package com.jakewharton.rxbinding.support.v4.view;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import com.jakewharton.rxbinding.internal.Functions;
import com.jakewharton.rxbinding.view.MenuItemActionViewEvent;

import rx.Observable;
import rx.functions.Func1;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables}
 * for {@link android.support.v4.view.MenuItemCompat}.
 */
public final class RxMenuItemCompat {

  /**
   * Create an observable of action view events for {@code menuItem}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code menuItem}.
   * Unsubscribe to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link MenuItem#setOnActionExpandListener} to
   * observe action view events. Only one observable can be used for a menu item at a time.
   */
  @CheckResult @NonNull
  public static Observable<MenuItemActionViewEvent> actionViewEvents(@NonNull MenuItem menuItem) {
    checkNotNull(menuItem, "menuItem == null");
    return Observable.create(new MenuItemActionViewEventOnSubscribe(menuItem,
        Functions.FUNC1_ALWAYS_TRUE));
  }

  /**
   * Create an observable of action view events for {@code menuItem}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code menuItem}.
   * Unsubscribe to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link MenuItem#setOnActionExpandListener} to
   * observe action view events. Only one observable can be used for a menu item at a time.
   *
   * @param handled Function invoked with each value to determine the return value of the
   * underlying {@link MenuItem.OnActionExpandListener}.
   */
  @CheckResult @NonNull
  public static Observable<MenuItemActionViewEvent> actionViewEvents(@NonNull MenuItem menuItem,
      @NonNull Func1<? super MenuItemActionViewEvent, Boolean> handled) {
    checkNotNull(menuItem, "menuItem == null");
    checkNotNull(handled, "handled == null");
    return Observable.create(new MenuItemActionViewEventOnSubscribe(menuItem, handled));
  }

  private RxMenuItemCompat() {
    throw new AssertionError("No instances.");
  }
}
