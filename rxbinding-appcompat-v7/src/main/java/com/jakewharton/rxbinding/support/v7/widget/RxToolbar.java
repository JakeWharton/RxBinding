package com.jakewharton.rxbinding.support.v7.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import rx.Observable;
import rx.functions.Action1;

/**
 * Static factory methods for creating {@linkplain Observable observables} and {@linkplain Action1
 * actions} for {@link Toolbar}.
 */
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

  private RxToolbar() {
    throw new AssertionError("No instances.");
  }
}
