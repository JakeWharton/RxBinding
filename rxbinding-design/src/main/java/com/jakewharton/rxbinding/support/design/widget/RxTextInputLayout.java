package com.jakewharton.rxbinding.support.design.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import rx.functions.Action1;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Action1 actions} for {@link TextInputLayout}.
 */
public final class RxTextInputLayout {

  /**
   * An action which sets the counterEnabled property of {@code view} with a boolean value.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Action1<? super Boolean> counterEnabled(@NonNull final TextInputLayout view) {
    checkNotNull(view, "view == null");
    return new Action1<Boolean>() {
      @Override public void call(Boolean enable) {
        view.setCounterEnabled(enable);
      }
    };
  }

  /**
   * An action which sets the counterMaxLength property of {@code view} with an integer value.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Action1<? super Integer> counterMaxLength(@NonNull final TextInputLayout view) {
    checkNotNull(view, "view == null");
    return new Action1<Integer>() {
      @Override public void call(Integer maxLength) {
        view.setCounterMaxLength(maxLength);
      }
    };
  }

  /**
   * An action which sets the error text of {@code view} with a character sequence.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Action1<? super CharSequence> error(@NonNull final TextInputLayout view) {
    checkNotNull(view, "view == null");
    return new Action1<CharSequence>() {
      @Override
      public void call(CharSequence error) {
        view.setError(error);
      }
    };
  }

  /**
   * An action which sets the error text of {@code view} with a string resource.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Action1<? super Integer> errorRes(@NonNull final TextInputLayout view) {
    checkNotNull(view, "view == null");
    return new Action1<Integer>() {
      @Override
      public void call(Integer errorRes) {
        view.setError(view.getContext().getResources().getText(errorRes));
      }
    };
  }

  /**
   * An action which sets the hint property of {@code view} with character sequences.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Action1<? super CharSequence> hint(@NonNull final TextInputLayout view) {
    checkNotNull(view, "view == null");
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
    checkNotNull(view, "view == null");
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
