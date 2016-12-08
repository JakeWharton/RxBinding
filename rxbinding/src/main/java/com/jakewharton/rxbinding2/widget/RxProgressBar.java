package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.ProgressBar;
import io.reactivex.functions.Consumer;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

public final class RxProgressBar {
  /**
   * An action which increments the progress value of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Consumer<? super Integer> incrementProgressBy(@NonNull final ProgressBar view) {
    checkNotNull(view, "view == null");
    return new Consumer<Integer>() {
      @Override public void accept(Integer value) {
        view.incrementProgressBy(value);
      }
    };
  }

  /**
   * An action which increments the secondary progress value of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Consumer<? super Integer> incrementSecondaryProgressBy(
      @NonNull final ProgressBar view) {
    checkNotNull(view, "view == null");
    return new Consumer<Integer>() {
      @Override public void accept(Integer value) {
        view.incrementSecondaryProgressBy(value);
      }
    };
  }

  /**
   * An action which sets whether {@code view} is indeterminate.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Consumer<? super Boolean> indeterminate(@NonNull final ProgressBar view) {
    checkNotNull(view, "view == null");
    return new Consumer<Boolean>() {
      @Override public void accept(Boolean value) {
        view.setIndeterminate(value);
      }
    };
  }

  /**
   * An action which sets the max value of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Consumer<? super Integer> max(@NonNull final ProgressBar view) {
    checkNotNull(view, "view == null");
    return new Consumer<Integer>() {
      @Override public void accept(Integer value) {
        view.setMax(value);
      }
    };
  }

  /**
   * An action which sets the progress value of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Consumer<? super Integer> progress(@NonNull final ProgressBar view) {
    checkNotNull(view, "view == null");
    return new Consumer<Integer>() {
      @Override public void accept(Integer value) {
        view.setProgress(value);
      }
    };
  }

  /**
   * An action which sets the secondary progress value of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Consumer<? super Integer> secondaryProgress(@NonNull final ProgressBar view) {
    checkNotNull(view, "view == null");
    return new Consumer<Integer>() {
      @Override public void accept(Integer value) {
        view.setSecondaryProgress(value);
      }
    };
  }

  private RxProgressBar() {
    throw new AssertionError("No instances.");
  }
}
