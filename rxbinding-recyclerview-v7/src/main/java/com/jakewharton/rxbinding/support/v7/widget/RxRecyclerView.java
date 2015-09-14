package com.jakewharton.rxbinding.support.v7.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import rx.Observable;
import rx.functions.Action1;

/**
 * Static factory methods for creating {@linkplain Observable observables} and {@linkplain Action1
 * actions} for {@link RecyclerView}.
 */
public final class RxRecyclerView {

  /**
   * Create an observable of child attach state change events on {@code recyclerView}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code recyclerView}.
   * Unsubscribe to free this reference.
   */
  @CheckResult @NonNull
  public static Observable<RecyclerViewChildAttachStateChangeEvent> childAttachStateChangeEvents(
      @NonNull RecyclerView recyclerView) {
    return Observable.create(new RecyclerViewChildAttachStateChangeEventOnSubscribe(recyclerView));
  }

  /**
   * Create an observable of scroll events on {@code recyclerView}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code recyclerView}.
   * Unsubscribe to free this reference.
   */
  @CheckResult @NonNull
  public static Observable<RecyclerViewScrollEvent> scrollEvents(
      @NonNull RecyclerView recyclerView) {
    return Observable.create(new RecyclerViewScrollEventOnSubscribe(recyclerView));
  }

  /**
   * Create an observable of scroll state change events on {@code recyclerView}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code recyclerView}.
   * Unsubscribe to free this reference.
   */
  @CheckResult @NonNull
  public static Observable<RecyclerViewScrollStateChangeEvent> scrollStateChangeEvents(
      @NonNull RecyclerView recyclerView) {
    return Observable.create(new RecyclerViewScrollStateChangeEventOnSubscribe(recyclerView));
  }

  private RxRecyclerView() {
    throw new AssertionError("No instances.");
  }
}
