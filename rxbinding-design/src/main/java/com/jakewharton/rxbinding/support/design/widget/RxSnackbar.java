package com.jakewharton.rxbinding.support.design.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import rx.Observable;
import rx.functions.Action1;

/**
 * Static factory methods for creating {@linkplain Observable observables} and {@linkplain Action1
 * actions} for {@link Snackbar}.
 */
public final class RxSnackbar {
  /**
   * Create an observable which emits the dismiss events from {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Observable<Integer> dismisses(@NonNull Snackbar view) {
    return Observable.create(new SnackbarDismissesOnSubscribe(view));
  }

  private RxSnackbar() {
    throw new AssertionError("No instances.");
  }
}
