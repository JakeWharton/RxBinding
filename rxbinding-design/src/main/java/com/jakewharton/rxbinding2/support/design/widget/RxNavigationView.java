package com.jakewharton.rxbinding2.support.design.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import io.reactivex.Observable;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables} for
 * {@link NavigationView}.
 */
public final class RxNavigationView {
  /**
   * Create an observable which emits the selected item in {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Note:</em> If an item is already selected, it will be emitted immediately on subscribe.
   * This behavior assumes but does not enforce that the items are exclusively checkable.
   */
  @CheckResult @NonNull
  public static Observable<MenuItem> itemSelections(@NonNull NavigationView view) {
    checkNotNull(view, "view == null");
    return new NavigationViewItemSelectionsObservable(view);
  }

  private RxNavigationView() {
    throw new AssertionError("No instances.");
  }
}
