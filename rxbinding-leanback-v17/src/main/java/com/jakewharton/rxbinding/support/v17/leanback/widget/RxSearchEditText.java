package com.jakewharton.rxbinding.support.v17.leanback.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v17.leanback.widget.SearchEditText;

import rx.Observable;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables} and {@linkplain rx.functions.Action1
 * actions} for {@link SearchEditText}.
 */
public final class RxSearchEditText {
  /**
   * Create an observable which emits the keyboard dismiss events from {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Observable<Void> keyboardDismisses(@NonNull SearchEditText view) {
    checkNotNull(view, "view == null");
    return Observable.create(new SearchEditTextKeyboardDismissOnSubscribe(view));
  }

  private RxSearchEditText() {
    throw new AssertionError("No instances.");
  }
}
