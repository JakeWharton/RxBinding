package com.jakewharton.rxbinding2.support.design.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import io.reactivex.Observable;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables} for {@link AppBarLayout}.
 */
public final class RxAppBarLayout {

  /**
   * Create an observable which emits the offset change in {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Observable<Integer> offsetChanges(@NonNull AppBarLayout view) {
    checkNotNull(view, "view == null");
    return new AppBarLayoutOffsetChangeObservable(view);
  }

  private RxAppBarLayout() {
    throw new AssertionError("No instances.");
  }
}
