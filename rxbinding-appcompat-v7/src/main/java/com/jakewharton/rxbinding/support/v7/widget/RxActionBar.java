package com.jakewharton.rxbinding.support.v7.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import rx.Observable;
import rx.functions.Action1;

/**
 * Static factory methods for creating {@linkplain Observable observables} and {@linkplain Action1
 * actions} for {@link ActionBar}.
 */
public final class RxActionBar {

  /**
   * Create an observable of menu visibility change events in {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Observable<Boolean> menuVisibilityChanges(@NonNull ActionBar view) {
    return Observable.create(new ActionBarMenuVisibilityChangeOnSubscribe(view));
  }

  private RxActionBar() {
    throw new AssertionError("No instances.");
  }
}
