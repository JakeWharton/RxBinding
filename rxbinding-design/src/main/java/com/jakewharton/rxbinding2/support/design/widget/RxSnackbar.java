package com.jakewharton.rxbinding2.support.design.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import io.reactivex.Observable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables} for {@link Snackbar}.
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
    checkNotNull(view, "view == null");
    return new SnackbarDismissesObservable(view);
  }

  /**
   * Create an observable which emits the action click events from {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Observable<View> actionClicks(@NonNull Snackbar view, int resId) {
    checkNotNull(view, "view == null");
    return new SnackbarActionObservable(view, resId);
  }

  /**
   * Create an observable which emits the action click events from {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Observable<View> actionClicks(@NonNull Snackbar view, @NonNull CharSequence text) {
    checkNotNull(view, "view == null");
    checkNotNull(text, "text == null");
    return new SnackbarActionObservable(view, text);
  }

  private RxSnackbar() {
    throw new AssertionError("No instances.");
  }
}
