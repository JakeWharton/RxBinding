package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.AutoCompleteTextView;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

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

  /**
   * An action that sets the optional hint text that is displayed at the bottom of the the matching
   * list. This can be used as a cue to the user on how to best use the list, or to provide extra
   * information.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Consumer<? super CharSequence> completionHint(
      @NonNull final AutoCompleteTextView view) {
    checkNotNull(view, "view == null");
    return new Consumer<CharSequence>() {
      @Override public void accept(CharSequence completionHint) {
        view.setCompletionHint(completionHint);
      }
    };
  }

  /**
   * An action that specifies the minimum number of characters the user has to type in the edit box
   * before the drop down list is shown. When threshold is less than or equals 0, a threshold of 1
   * is applied.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Consumer<? super Integer> threshold(@NonNull final AutoCompleteTextView view) {
    checkNotNull(view, "view == null");
    return new Consumer<Integer>() {
      @Override public void accept(Integer threshold) {
        view.setThreshold(threshold);
      }
    };
  }

  private RxAutoCompleteTextView() {
    throw new AssertionError("No instances.");
  }
}
