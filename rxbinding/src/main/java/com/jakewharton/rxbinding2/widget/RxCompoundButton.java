package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.CompoundButton;


import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables} and {@linkplain Consumer
 * actions} for {@link CompoundButton}.
 */
public final class RxCompoundButton {
  /**
   * Create an observable of booleans representing the checked state of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link CompoundButton#setOnCheckedChangeListener}
   * to observe checked changes. Only one observable can be used for a view at a time.
   * <p>
   * <em>Note:</em> A value will be emitted immediately on subscribe.
   */
  @CheckResult @NonNull
  public static Observable<Boolean> checkedChanges(@NonNull CompoundButton view) {
    checkNotNull(view, "view == null");
    return new CompoundButtonCheckedChangeObservable(view);
  }

  /**
   * An action which sets the checked property of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Consumer<? super Boolean> checked(@NonNull final CompoundButton view) {
    checkNotNull(view, "view == null");
    return new Consumer<Boolean>() {
      @Override
      public void accept(Boolean value) throws Exception {
        view.setChecked(value);
      }
    };
  }

  /**
   * An action which sets the toggles property of {@code view} with each value.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Consumer<? super Object> toggle(@NonNull final CompoundButton view) {
    checkNotNull(view, "view == null");
    return new Consumer<Object>() {
      @Override public void accept(Object value) {
        view.toggle();
      }
    };
  }

  private RxCompoundButton() {
    throw new AssertionError("No instances.");
  }
}
