package com.jakewharton.rxbinding2.support.design.widget;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import com.jakewharton.rxbinding2.internal.GenericTypeNullable;
import io.reactivex.functions.Consumer;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Consumer actions} for {@link TextInputLayout}.
 */
public final class RxTextInputLayout {

  /**
   * An action which sets the counterEnabled property of {@code view} with a boolean value.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   *
   * @deprecated Use view::setCounterEnabled method reference.
   */
  @Deprecated
  @CheckResult @NonNull
  public static Consumer<? super Boolean> counterEnabled(@NonNull TextInputLayout view) {
    checkNotNull(view, "view == null");
    return view::setCounterEnabled;
  }

  /**
   * An action which sets the counterMaxLength property of {@code view} with an integer value.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   *
   * @deprecated Use view::setCounterMaxLength method reference.
   */
  @Deprecated
  @CheckResult @NonNull
  public static Consumer<? super Integer> counterMaxLength(@NonNull TextInputLayout view) {
    checkNotNull(view, "view == null");
    return view::setCounterMaxLength;
  }

  /**
   * An action which sets the error text of {@code view} with a character sequence.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   *
   * @deprecated Use view::setError method reference.
   */
  @Deprecated
  @CheckResult @NonNull @GenericTypeNullable
  public static Consumer<? super CharSequence> error(@NonNull TextInputLayout view) {
    checkNotNull(view, "view == null");
    return view::setError;
  }

  /**
   * An action which sets the error text of {@code view} with a string resource.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   *
   * @deprecated Use view::setError method reference.
   */
  @Deprecated
  @CheckResult @NonNull @GenericTypeNullable
  public static Consumer<? super Integer> errorRes(@NonNull TextInputLayout view) {
    checkNotNull(view, "view == null");
    return errorRes -> view.setError(view.getContext().getResources().getText(errorRes));
  }

  /**
   * An action which sets the hint property of {@code view} with character sequences.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   *
   * @deprecated Use view::setHint method reference.
   */
  @Deprecated
  @CheckResult @NonNull
  public static Consumer<? super CharSequence> hint(@NonNull TextInputLayout view) {
    checkNotNull(view, "view == null");
    return view::setHint;
  }

  /**
   * An action which sets the hint property of {@code view} string resource IDs.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   *
   * @deprecated Use view::setHint method reference.
   */
  @Deprecated
  @CheckResult @NonNull
  public static Consumer<? super Integer> hintRes(@NonNull TextInputLayout view) {
    checkNotNull(view, "view == null");
    return hintRes -> view.setHint(view.getContext().getResources().getText(hintRes));
  }

  private RxTextInputLayout() {
    throw new AssertionError("No instances.");
  }
}
