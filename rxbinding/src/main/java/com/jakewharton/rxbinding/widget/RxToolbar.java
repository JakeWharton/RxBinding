package com.jakewharton.rxbinding.widget;

import android.annotation.TargetApi;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.Toolbar;
import android.view.MenuItem;
import rx.Observable;
import rx.functions.Action1;

import static android.os.Build.VERSION_CODES.LOLLIPOP;

/**
 * Static factory methods for creating {@linkplain Observable observables} and {@linkplain Action1
 * actions} for {@link Toolbar}.
 */
@TargetApi(LOLLIPOP)
public final class RxToolbar {
  /**
   * Create an observable which emits the clicked item in {@code view}'s menu.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Observable<MenuItem> itemClicks(@NonNull Toolbar view) {
    return Observable.create(new ToolbarItemClickOnSubscribe(view));
  }

  /**
   * Create an observable which emits on {@code view} navigation click events. The emitted value is
   * unspecified and should only be used as notification.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link Toolbar#setNavigationOnClickListener}
   * to observe clicks. Only one observable can be used for a view at a time.
   */
  @CheckResult @NonNull
  public static Observable<Object> navigationClicks(@NonNull Toolbar view) {
    return Observable.create(new ToolbarNavigationClickOnSubscribe(view));
  }

  private RxToolbar() {
    throw new AssertionError("No instances.");
  }
}
