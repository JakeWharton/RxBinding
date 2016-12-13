package com.jakewharton.rxbinding2.support.design.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import com.jakewharton.rxbinding2.internal.GenericTypeNullable;
import io.reactivex.functions.Consumer;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Consumer actions} for {@link TextInputLayout}.
 */
public final class RxTextInputLayout {

  /**
   * An action which sets the counterEnabled property of {@code view} with a boolean value.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Consumer<? super Boolean> counterEnabled(@NonNull final TextInputLayout view) {
    checkNotNull(view, "view == null");
    return new Consumer<Boolean>() {
      @Override public void accept(Boolean enable) {
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
  public static Consumer<? super Integer> counterMaxLength(@NonNull final TextInputLayout view) {
    checkNotNull(view, "view == null");
    return new Consumer<Integer>() {
      @Override public void accept(Integer maxLength) {
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
  @CheckResult @NonNull @GenericTypeNullable
  public static Consumer<? super CharSequence> error(@NonNull final TextInputLayout view) {
    checkNotNull(view, "view == null");
    return new Consumer<CharSequence>() {
      @Override
      public void accept(CharSequence error) {
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
  @CheckResult @NonNull @GenericTypeNullable
  public static Consumer<? super Integer> errorRes(@NonNull final TextInputLayout view) {
    checkNotNull(view, "view == null");
    return new Consumer<Integer>() {
      @Override
      public void accept(Integer errorRes) {
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
  public static Consumer<? super CharSequence> hint(@NonNull final TextInputLayout view) {
    checkNotNull(view, "view == null");
    return new Consumer<CharSequence>() {
      @Override public void accept(CharSequence hint) {
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
  public static Consumer<? super Integer> hintRes(@NonNull final TextInputLayout view) {
    checkNotNull(view, "view == null");
    return new Consumer<Integer>() {
      @Override public void accept(Integer hintRes) {
        view.setHint(view.getContext().getResources().getText(hintRes));
      }
    };
  }

  private RxTextInputLayout() {
    throw new AssertionError("No instances.");
  }
}
