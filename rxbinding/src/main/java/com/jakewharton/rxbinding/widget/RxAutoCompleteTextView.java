package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.AutoCompleteTextView;

import rx.Observable;
import rx.functions.Action1;

/**
 * Static factory methods for creating {@linkplain Observable observables} and {@linkplain Action1
 * actions} for {@link AutoCompleteTextView}.
 */
public final class RxAutoCompleteTextView {
  /**
   * Create an observable of item click events on {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult
  @NonNull
  public static Observable<AdapterViewItemClickEvent> itemClickEvents(@NonNull AutoCompleteTextView view) {
    return Observable.create(new AutoCompleteTextViewItemClickEventOnSubscribe(view));
  }

  private RxAutoCompleteTextView() {
    throw new AssertionError("No instances.");
  }
}
