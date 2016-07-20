package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.AbsListView;
import rx.Observable;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

public final class RxAbsListView {
  /**
   * Create an observable of scroll events on {@code absListView}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code absListView}.
   * Unsubscribe to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses
   * {@link AbsListView#setOnScrollListener(AbsListView.OnScrollListener)} to observe scroll
   * changes. Only one observable can be used for a view at a time.
   */
  @CheckResult @NonNull
  public static Observable<AbsListViewScrollEvent> scrollEvents(@NonNull AbsListView absListView) {
    checkNotNull(absListView, "absListView == null");
    return Observable.create(new AbsListViewScrollEventOnSubscribe(absListView));
  }

  private RxAbsListView() {
    throw new AssertionError("No instances.");
  }
}
