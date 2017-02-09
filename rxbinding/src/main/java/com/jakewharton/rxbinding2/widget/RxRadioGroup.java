package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.RadioGroup;
import com.jakewharton.rxbinding2.InitialValueObservable;
import io.reactivex.functions.Consumer;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkNotNull;

public final class RxRadioGroup {
  /**
   * Create an observable of the checked view ID changes in {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Note:</em> A value will be emitted immediately on subscribe.
   */
  @CheckResult @NonNull
  public static InitialValueObservable<Integer> checkedChanges(@NonNull RadioGroup view) {
    checkNotNull(view, "view == null");
    return new RadioGroupCheckedChangeObservable(view);
  }

  /**
   * An action which sets the checked child of {@code view} with ID. Passing {@code -1} will clear
   * any checked view.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Consumer<? super Integer> checked(@NonNull final RadioGroup view) {
    checkNotNull(view, "view == null");
    return new Consumer<Integer>() {
      @Override public void accept(Integer value) {
        if (value == -1) {
          view.clearCheck();
        } else {
          view.check(value);
        }
      }
    };
  }

  private RxRadioGroup() {
    throw new AssertionError("No instances.");
  }
}
