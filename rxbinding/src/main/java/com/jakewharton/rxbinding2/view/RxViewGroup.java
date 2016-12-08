package com.jakewharton.rxbinding2.view;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import io.reactivex.Observable;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables} for {@link ViewGroup}.
 */
public final class RxViewGroup {
  /**
   * Create an observable of hierarchy change events for {@code viewGroup}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code viewGroup}.
   * Unsubscribe to free this reference.
   */
  @CheckResult @NonNull
  public static Observable<ViewGroupHierarchyChangeEvent> changeEvents(
      @NonNull ViewGroup viewGroup) {
    checkNotNull(viewGroup, "viewGroup == null");
    return new ViewGroupHierarchyChangeEventObservable(viewGroup);
  }

  private RxViewGroup() {
    throw new AssertionError("No instances.");
  }
}
