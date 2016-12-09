package com.jakewharton.rxbinding2.support.v7.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v7.widget.ActionMenuView;
import android.view.MenuItem;
import io.reactivex.Observable;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables} for
 * {@link android.widget.ActionMenuView}.
 */

public final class RxActionMenuView {
  /**
   * Create an observable which emits the clicked menu item in {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}.
   * Unsubscribe to free this reference.
   */
  @CheckResult @NonNull
  public static Observable<MenuItem> itemClicks(@NonNull ActionMenuView view) {
    checkNotNull(view, "view == null");
    return new ActionMenuViewItemClickObservable(view);
  }

  private RxActionMenuView() {
    throw new AssertionError("No instances.");
  }
}
