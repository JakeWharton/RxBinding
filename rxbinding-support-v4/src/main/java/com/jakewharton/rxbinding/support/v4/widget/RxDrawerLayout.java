package com.jakewharton.rxbinding.support.v4.widget;

import android.support.v4.widget.DrawerLayout;
import rx.Observable;
import rx.functions.Action1;

public final class RxDrawerLayout {
  /**
   * Create an observable of the open state of the drawer of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Note:</em> A value will be emitted immediately on subscribe.
   */
  public static Observable<Boolean> drawerOpen(DrawerLayout view, int gravity) {
    return Observable.create(new DrawerLayoutDrawerOpenedOnSubscribe(view, gravity));
  }

  /**
   * An action which sets whether the drawer with {@code gravity} of {@code view} is open.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  public static Action1<? super Boolean> open(final DrawerLayout view, final int gravity) {
    return new Action1<Boolean>() {
      @Override public void call(Boolean aBoolean) {
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
