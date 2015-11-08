package com.jakewharton.rxbinding.support.v4.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import rx.Observable;
import rx.functions.Action1;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

public final class RxSwipeRefreshLayout {
  /**
   * Create an observable of refresh events on {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Observable<Void> refreshes(@NonNull SwipeRefreshLayout view) {
    checkNotNull(view, "view == null");
    return Observable.create(new SwipeRefreshLayoutRefreshOnSubscribe(view));
  }

  /**
   * An action which sets whether the layout is showing the refreshing indicator.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Action1<? super Boolean> refreshing(@NonNull final SwipeRefreshLayout view) {
    checkNotNull(view, "view == null");
    return new Action1<Boolean>() {
      @Override public void call(Boolean value) {
        view.setRefreshing(value);
      }
    };
  }

  private RxSwipeRefreshLayout() {
    throw new AssertionError("No instances.");
  }
}
