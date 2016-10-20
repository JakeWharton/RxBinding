package com.jakewharton.rxbinding.support.design.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import rx.Observable;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables} for
 * {@link BottomNavigationView}.
 */
public final class RxBottomNavigationView {

  /**
   * Create an observable which emits the selected item in {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Note:</em> If an item is already selected, it will be emitted immediately on subscribe.
   * This behavior assumes but does not enforce that the items are exclusively checkable.
   */
  @CheckResult @NonNull public static Observable<MenuItem> itemSelections(
      @NonNull BottomNavigationView view) {
    checkNotNull(view, "view == null");
    return Observable.create(new BottomNavigationViewItemSelectionsOnSubscribe(view));
  }

  private RxBottomNavigationView() {
    throw new AssertionError("No instances.");
  }
}
