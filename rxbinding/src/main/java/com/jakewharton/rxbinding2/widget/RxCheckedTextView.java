package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.CheckedTextView;
import io.reactivex.functions.Consumer;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Consumer actions} for {@link CheckedTextView}.
 */
public final class RxCheckedTextView {

  /**
   * A consumer which sets the checked property of {@code view} with a boolean value.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Consumer<? super Boolean> check(@NonNull final CheckedTextView view) {
    checkNotNull(view, "view == null");
    return new Consumer<Boolean>() {
      @Override public void accept(Boolean check) {
        view.setChecked(check);
      }
    };
  }

  private RxCheckedTextView() {
    throw new AssertionError("No instances.");
  }
}
