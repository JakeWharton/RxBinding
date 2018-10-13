package com.jakewharton.rxbinding2.widget;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import android.widget.CheckedTextView;
import io.reactivex.functions.Consumer;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Consumer actions} for {@link CheckedTextView}.
 */
public final class RxCheckedTextView {

  /**
   * A consumer which sets the checked property of {@code view} with a boolean value.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   *
   * @deprecated Use view::setChecked method reference.
   */
  @Deprecated
  @CheckResult @NonNull
  public static Consumer<? super Boolean> check(@NonNull CheckedTextView view) {
    checkNotNull(view, "view == null");
    return view::setChecked;
  }

  private RxCheckedTextView() {
    throw new AssertionError("No instances.");
  }
}
