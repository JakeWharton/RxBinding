package com.jakewharton.rxbinding.support.design.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import rx.functions.Action1;

/**
 * Static factory methods for creating {@linkplain Action1 actions} for {@link TextInputLayout }.
 */
public final class RxTextInputLayout {

  /**
   * An action which sets the hint property of {@code view} with character sequences.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Action1<? super CharSequence> hint(@NonNull final TextInputLayout view) {
    return new Action1<CharSequence>() {
      @Override public void call(CharSequence hint) {
        view.setHint(hint);
      }
    };
  }

  /**
   * An action which sets the hint property of {@code view} string resource IDs.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Action1<? super Integer> hintRes(@NonNull final TextInputLayout view) {
    return new Action1<Integer>() {
      @Override public void call(Integer hintRes) {
        view.setHint(view.getContext().getResources().getText(hintRes));
      }
    };
  }

  private RxTextInputLayout() {
    throw new AssertionError("No instances.");
  }
}
