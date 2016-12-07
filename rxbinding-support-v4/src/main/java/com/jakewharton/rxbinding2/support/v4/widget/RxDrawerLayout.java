package com.jakewharton.rxbinding2.support.v4.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

public final class RxDrawerLayout {
  /**
   * Create an observable of the open state of the drawer of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Note:</em> A value will be emitted immediately on subscribe.
   */
  @CheckResult @NonNull public static Observable<Boolean> drawerOpen(@NonNull DrawerLayout view,
      int gravity) {
    checkNotNull(view, "view == null");
    return new DrawerLayoutDrawerOpenedObservable(view, gravity);
  }

  /**
   * An action which sets whether the drawer with {@code gravity} of {@code view} is open.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull public static Consumer<? super Boolean> open(
      @NonNull final DrawerLayout view, final int gravity) {
    checkNotNull(view, "view == null");
    return new Consumer<Boolean>() {
      @Override public void accept(Boolean aBoolean) {
        if (aBoolean) {
          view.openDrawer(gravity);
        } else {
          view.closeDrawer(gravity);
        }
      }
    };
  }

  private RxDrawerLayout() {
    throw new AssertionError("No instances.");
  }
}
