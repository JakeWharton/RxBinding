package com.jakewharton.rxbinding.support.v7.widget;

import android.support.annotation.CheckResult;
import android.support.v7.widget.RecyclerView;

import rx.Observable;
import rx.functions.Action1;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables} and {@linkplain Action1
 * actions} for {@link RecyclerView}.
 */
public class RxRecyclerView {
  /**
   * Create an observable of scroll events on {@code recyclerView}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code recyclerView}.
   * Unsubscribe to free this reference.
   */
  @CheckResult
  public static Observable<RecyclerViewScrollEvent> scrollEvents(RecyclerView recyclerView) {
    checkNotNull(recyclerView, "view == null");
    return Observable.create(new RecyclerViewScrollEventOnSubscribe(recyclerView));
  }

  /**
   * Create an observable of scroll state change events on {@code recyclerView}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code recyclerView}.
   * Unsubscribe to free this reference.
   */
  @CheckResult
  public static Observable<RecyclerViewScrollStateChangeEvent> scrollStateChangeEvents(RecyclerView recyclerView) {
    checkNotNull(recyclerView, "view == null");
    return Observable.create(new RecyclerViewScrollStateChangeEventOnSubscribe(recyclerView));
  }

  private RxRecyclerView() {
    throw new AssertionError("No instances.");
  }
}
