package com.jakewharton.rxbinding.support.v7.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import rx.Observable;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables} for {@link RecyclerView}.
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
      @NonNull RecyclerView view) {
    checkNotNull(view, "view == null");
    return Observable.create(new RecyclerViewChildAttachStateChangeEventOnSubscribe(view));
  }

  /**
   * Create an observable of scroll events on {@code recyclerView}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code recyclerView}.
   * Unsubscribe to free this reference.
   */
  @CheckResult @NonNull
  public static Observable<RecyclerViewScrollEvent> scrollEvents(
      @NonNull RecyclerView view) {
    checkNotNull(view, "view == null");
    return Observable.create(new RecyclerViewScrollEventOnSubscribe(view));
  }

  /**
   * Create an observable of scroll state changes on {@code recyclerView}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code recyclerView}.
   * Unsubscribe to free this reference.
   */
  @CheckResult @NonNull
  public static Observable<Integer> scrollStateChanges(@NonNull RecyclerView view) {
    checkNotNull(view, "view == null");
    return Observable.create(new RecyclerViewScrollStateChangeOnSubscribe(view));
  }

  /**
   * Create an observable of click events on {@code recyclerView} items.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code recyclerView}.
   * Unsubscribe to free this reference.
   */
  @CheckResult @NonNull
  public static Observable<Integer> itemClick(@NonNull RecyclerView view) {
    checkNotNull(view, "view == null");
    return Observable.create(new RecyclerViewItemClickOnSubscribe(view));
  }

  /**
   * Create an observable of long click events on {@code recyclerView} items.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code recyclerView}.
   * Unsubscribe to free this reference.
   */
  @CheckResult @NonNull
  public static Observable<Integer> itemLongClick(@NonNull RecyclerView view) {
    checkNotNull(view, "view == null");
    return Observable.create(new RecyclerViewItemLongClickOnSubscribe(view));
  }

  private RxRecyclerView() {
    throw new AssertionError("No instances.");
  }
}
