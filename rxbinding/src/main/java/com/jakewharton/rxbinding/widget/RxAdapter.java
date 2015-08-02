package com.jakewharton.rxbinding.widget;

import android.widget.Adapter;
import rx.Observable;

/**
 * Static factory methods for creating {@linkplain Observable observables} for {@link Adapter}.
 */
public final class RxAdapter {
  /**
   * Create an observable of data change events for {@code adapter}.
   * <p>
   * <em>Note:</em> A value will be emitted immediately on subscribe.
   */
  public static <T extends Adapter> Observable<T> dataChanges(T adapter) {
    return Observable.create(new AdapterDataChangeOnSubscribe<>(adapter));
  }

  private RxAdapter() {
    throw new AssertionError("No instances.");
  }
}
