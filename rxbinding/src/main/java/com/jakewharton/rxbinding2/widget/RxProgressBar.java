package com.jakewharton.rxbinding2.widget;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import android.widget.ProgressBar;
import io.reactivex.functions.Consumer;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkNotNull;

public final class RxProgressBar {
  /**
   * An action which increments the progress value of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   *
   * @deprecated Use view::incrementProgressBy method reference.
   */
  @Deprecated
  @CheckResult @NonNull
  public static Consumer<? super Integer> incrementProgressBy(@NonNull ProgressBar view) {
    checkNotNull(view, "view == null");
    return view::incrementProgressBy;
  }

  /**
   * An action which increments the secondary progress value of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   *
   * @deprecated Use view::incrementSecondaryProgressBy method reference.
   */
  @Deprecated
  @CheckResult @NonNull
  public static Consumer<? super Integer> incrementSecondaryProgressBy(@NonNull ProgressBar view) {
    checkNotNull(view, "view == null");
    return view::incrementSecondaryProgressBy;
  }

  /**
   * An action which sets whether {@code view} is indeterminate.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   *
   * @deprecated Use view::setIndeterminate method reference.
   */
  @Deprecated
  @CheckResult @NonNull
  public static Consumer<? super Boolean> indeterminate(@NonNull ProgressBar view) {
    checkNotNull(view, "view == null");
    return view::setIndeterminate;
  }

  /**
   * An action which sets the max value of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   *
   * @deprecated Use view::setMax method reference.
   */
  @Deprecated
  @CheckResult @NonNull
  public static Consumer<? super Integer> max(@NonNull ProgressBar view) {
    checkNotNull(view, "view == null");
    return view::setMax;
  }

  /**
   * An action which sets the progress value of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   *
   * @deprecated Use view::setProgress method reference.
   */
  @Deprecated
  @CheckResult @NonNull
  public static Consumer<? super Integer> progress(@NonNull ProgressBar view) {
    checkNotNull(view, "view == null");
    return view::setProgress;
  }

  /**
   * An action which sets the secondary progress value of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   *
   * @deprecated Use view::setSecondaryProgress method reference.
   */
  @Deprecated
  @CheckResult @NonNull
  public static Consumer<? super Integer> secondaryProgress(@NonNull ProgressBar view) {
    checkNotNull(view, "view == null");
    return view::setSecondaryProgress;
  }

  private RxProgressBar() {
    throw new AssertionError("No instances.");
  }
}
