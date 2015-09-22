package com.jakewharton.rxbinding.support.design.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import rx.Observable;

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
    return Observable.create(new AppBarLayoutOffsetChangeOnSubscribe(view));
  }

  /**
   * Create an observable which emits offsetChange events for the {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Observable<AppBarLayoutOffsetChangeEvent> offsetChangeEvents(@NonNull AppBarLayout view) {
    return Observable.create(new AppBarLayoutOffsetChangeEventOnSubscribe(view));
  }

  private RxAppBarLayout() {
    throw new AssertionError("No instances.");
  }
}
