package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.Adapter;
import rx.Observable;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables} for {@link Adapter}.
 */
public final class RxAdapter {
  /**
   * Create an observable of data change events for {@code adapter}.
   * <p>
   * <em>Note:</em> A value will be emitted immediately on subscribe.
   */
  @CheckResult @NonNull
  public static <T extends Adapter> Observable<T> dataChanges(@NonNull T adapter) {
    checkNotNull(adapter, "adapter == null");
    return Observable.create(new AdapterDataChangeOnSubscribe<>(adapter));
  }

  private RxAdapter() {
    throw new AssertionError("No instances.");
  }
}
