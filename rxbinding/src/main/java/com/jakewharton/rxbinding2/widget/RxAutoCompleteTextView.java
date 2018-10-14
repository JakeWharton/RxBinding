package com.jakewharton.rxbinding2.widget;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import android.widget.AutoCompleteTextView;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables} and {@linkplain Consumer
 * actions} for {@link AutoCompleteTextView}.
 */
public final class RxAutoCompleteTextView {
  /**
   * Create an observable of item click events on {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Observable<AdapterViewItemClickEvent> itemClickEvents(
      @NonNull AutoCompleteTextView view) {
    checkNotNull(view, "view == null");
    return new AutoCompleteTextViewItemClickEventObservable(view);
  }

  private RxAutoCompleteTextView() {
    throw new AssertionError("No instances.");
  }
}
