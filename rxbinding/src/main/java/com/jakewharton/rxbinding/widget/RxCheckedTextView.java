package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.CheckedTextView;
import rx.functions.Action1;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Action1 actions} for {@link CheckedTextView}.
 */
public final class RxCheckedTextView {

  /**
   * An action which sets the checked property of {@code view} with a boolean value.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Action1<? super Boolean> check(@NonNull final CheckedTextView view) {
    checkNotNull(view, "view == null");
    return new Action1<Boolean>() {
      @Override public void call(Boolean check) {
        view.setChecked(check);
      }
    };
  }

  private RxCheckedTextView() {
    throw new AssertionError("No instances.");
  }
}
