package com.jakewharton.rxbinding.widget;

import android.widget.CompoundButton;
import rx.Observable;
import rx.functions.Action1;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables} and {@linkplain Action1
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
  public static Observable<Boolean> checkedChanges(CompoundButton view) {
    checkNotNull(view, "view == null");
    return Observable.create(new CompoundButtonCheckedChangeOnSubscribe(view));
  }

  /**
   * Create an observable of checked change events for {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link CompoundButton#setOnCheckedChangeListener}
   * to observe checked changes. Only one observable can be used for a view at a time.
   * <p>
   * <em>Note:</em> A value will be emitted immediately on subscribe.
   */
  public static Observable<CompoundButtonCheckedChangeEvent> checkedChangeEvents(
      CompoundButton view) {
    checkNotNull(view, "view == null");
    return Observable.create(new CompoundButtonCheckedChangeEventOnSubscribe(view));
  }

  /**
   * An action which sets the checked property of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  public static Action1<? super Boolean> checked(final CompoundButton view) {
    checkNotNull(view, "view == null");
    return new Action1<Boolean>() {
      @Override public void call(Boolean value) {
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
  public static Action1<? super Object> toggle(final CompoundButton view) {
    checkNotNull(view, "view == null");
    return new Action1<Object>() {
      @Override public void call(Object value) {
        view.toggle();
      }
    };
  }

  private RxCompoundButton() {
    throw new AssertionError("No instances.");
  }
}
