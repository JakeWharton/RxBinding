package com.jakewharton.rxbinding2.support.v4.view;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

public final class RxViewPager {
  /**
   * Create an observable of scroll state change events on {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   */
  @CheckResult @NonNull public static Observable<Integer> pageScrollStateChanges(
      @NonNull ViewPager view) {
    checkNotNull(view, "view == null");
    return new ViewPagerPageScrollStateChangedObservable(view);
  }

  /**
   * Create an observable of page selected events on {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Note:</em> A value will be emitted immediately on subscribe.
   */
  @CheckResult @NonNull public static Observable<Integer> pageSelections(@NonNull ViewPager view) {
    checkNotNull(view, "view == null");
    return new ViewPagerPageSelectedObservable(view);
  }

  /**
   * An action which sets the current item of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull public static Consumer<? super Integer> currentItem(
      @NonNull final ViewPager view) {
    checkNotNull(view, "view == null");
    return new Consumer<Integer>() {
      @Override public void accept(Integer value) {
        view.setCurrentItem(value);
      }
    };
  }

  private RxViewPager() {
    throw new AssertionError("No instances.");
  }
}
