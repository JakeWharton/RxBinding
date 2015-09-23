package com.jakewharton.rxbinding.support.v4.view;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import rx.Observable;

public final class RxViewPager {
  /**
   * Create an observable of page selected events on {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Note:</em> A value will be emitted immediately on subscribe.
   */
  @CheckResult @NonNull
  public static Observable<Integer> pageSelections(@NonNull ViewPager view) {
    return Observable.create(new ViewPagerPageSelectedOnSubscribe(view));
  }

  private RxViewPager() {
    throw new AssertionError("No instances.");
  }
}
