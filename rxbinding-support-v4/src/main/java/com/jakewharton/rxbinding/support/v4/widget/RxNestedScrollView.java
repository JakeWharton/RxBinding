package com.jakewharton.rxbinding.support.v4.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;

import com.jakewharton.rxbinding.view.ViewScrollChangeEvent;

import rx.Observable;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

public final class RxNestedScrollView {
  /**
   * Create an observable of scroll-change events for {@code nestedScrollView}.
   * <p/>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code nestedScrollView}.
   * Unsubscribe to free this reference.
   */
  @CheckResult @NonNull public static Observable<ViewScrollChangeEvent> scrollChangeEvents(
      @NonNull NestedScrollView nestedScrollView) {
    checkNotNull(nestedScrollView, "nestedScrollView == null");
    return Observable.create(new NestedScrollViewScrollChangeEventOnSubscribe(nestedScrollView));
  }

  private RxNestedScrollView() {
    throw new AssertionError("No instances.");
  }
}
