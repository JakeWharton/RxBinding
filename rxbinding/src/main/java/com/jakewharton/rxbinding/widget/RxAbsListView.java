package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.AbsListView;
import rx.Observable;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

public final class RxAbsListView {
  /**
   * Create an observable of scroll events on {@code listView}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code listView}.
   * Unsubscribe to free this reference.
   * * <p>
   * <em>Warning:</em> The created observable uses
   * {@link AbsListView#setOnScrollListener(AbsListView.OnScrollListener)}  to observe scroll
   * changes. Only one observable can be used for a view at a time.
   * <p>
   */
  @CheckResult @NonNull public static Observable<AbsListViewScrollEvent> scrollEvents(
      @NonNull AbsListView listView) {
    checkNotNull(listView, "listView == null");

    return Observable.create(new AbsListViewScrollEventOnSubscribe(listView));
  }

  private RxAbsListView() {
    throw new AssertionError("No instances.");
  }
}
