package com.jakewharton.rxbinding.support.v7.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckedTextView;
import rx.functions.Action1;

/**
 * Static factory methods for creating {@linkplain Action1 actions} for {@link AppCompatCheckedTextView}.
 */
public final class RxAppCompatCheckedTextView {

  /**
   * An action which sets the checked property of {@code view} with a boolean value.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Action1<? super Boolean> check(@NonNull final AppCompatCheckedTextView view) {
    return new Action1<Boolean>() {
      @Override public void call(Boolean check) {
        view.setChecked(check);
      }
    };
  }

  public RxAppCompatCheckedTextView() {
    throw new AssertionError("No Instances.");
  }
}
