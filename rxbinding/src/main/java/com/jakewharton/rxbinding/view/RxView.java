package com.jakewharton.rxbinding.view;

import android.support.annotation.CheckResult;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

import com.jakewharton.rxbinding.internal.Functions;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;

import static com.jakewharton.rxbinding.internal.Preconditions.checkArgument;
import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables} and {@linkplain Action1
 * actions} for {@link View}.
 */
public final class RxView {
  /**
   * Create an observable which emits on {@code view} attach events. The emitted value is
   * unspecified and should only be used as notification.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult
  public static Observable<Object> attaches(View view) {
    checkNotNull(view, "view == null");
    return Observable.create(new ViewAttachesOnSubscribe(view, true));
  }

  /**
   * Create an observable of attach and detach events on {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult
  public static Observable<ViewAttachEvent> attachEvents(View view) {
    checkNotNull(view, "view == null");
    return Observable.create(new ViewAttachEventOnSubscribe(view));
  }

  /**
   * Create an observable which emits on {@code view} detach events. The emitted value is
   * unspecified and should only be used as notification.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult
  public static Observable<Object> detaches(View view) {
    checkNotNull(view, "view == null");
    return Observable.create(new ViewAttachesOnSubscribe(view, false));
  }

  /**
   * Create an observable which emits on {@code view} click events. The emitted value is
   * unspecified and should only be used as notification.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link View#setOnClickListener} to observe
   * clicks. Only one observable can be used for a view at a time.
   */
  @CheckResult
  public static Observable<Object> clicks(View view) {
    checkNotNull(view, "view == null");
    return Observable.create(new ViewClickOnSubscribe(view));
  }

  /**
   * Create an observable of click events for {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link View#setOnClickListener} to observe
   * clicks. Only one observable can be used for a view at a time.
   */
  @CheckResult
  public static Observable<ViewClickEvent> clickEvents(View view) {
    checkNotNull(view, "view == null");
    return Observable.create(new ViewClickEventOnSubscribe(view));
  }

  /**
   * Create an observable of {@link DragEvent} for drags on {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link View#setOnDragListener} to observe
   * drags. Only one observable can be used for a view at a time.
   */
  @CheckResult
  public static Observable<DragEvent> drags(View view) {
    checkNotNull(view, "view == null");
    return Observable.create(new ViewDragOnSubscribe(view, Functions.FUNC1_ALWAYS_TRUE));
  }

  /**
   * Create an observable of {@link DragEvent} for {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link View#setOnDragListener} to observe
   * drags. Only one observable can be used for a view at a time.
   *
   * @param handled Function invoked with each value to determine the return value of the
   * underlying {@link View.OnDragListener}.
   */
  @CheckResult
  public static Observable<DragEvent> drags(View view, Func1<DragEvent, Boolean> handled) {
    checkNotNull(view, "view == null");
    checkNotNull(handled, "handled == null");
    return Observable.create(new ViewDragOnSubscribe(view, handled));
  }

  /**
   * Create an observable of drag events for {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link View#setOnDragListener} to observe
   * drags. Only one observable can be used for a view at a time.
   */
  @CheckResult
  public static Observable<ViewDragEvent> dragEvents(View view) {
    checkNotNull(view, "view == null");
    return Observable.create(new ViewDragEventOnSubscribe(view, Functions.FUNC1_ALWAYS_TRUE));
  }

  /**
   * Create an observable of drag events for {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link View#setOnDragListener} to observe
   * drags. Only one observable can be used for a view at a time.
   *
   * @param handled Function invoked with each value to determine the return value of the
   * underlying {@link View.OnDragListener}.
   */
  @CheckResult
  public static Observable<ViewDragEvent> dragEvents(View view,
      Func1<ViewDragEvent, Boolean> handled) {
    checkNotNull(view, "view == null");
    checkNotNull(handled, "handled == null");
    return Observable.create(new ViewDragEventOnSubscribe(view, handled));
  }

  /**
   * Create an observable of booleans representing the focus of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link View#setOnFocusChangeListener} to observe
   * focus change. Only one observable can be used for a view at a time.
   * <p>
   * <em>Note:</em> A value will be emitted immediately on subscribe.
   */
  @CheckResult
  public static Observable<Boolean> focusChanges(View view) {
    checkNotNull(view, "view == null");
    return Observable.create(new ViewFocusChangeOnSubscribe(view));
  }


  /**
   * Create an observable of focus-change events for {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link View#setOnFocusChangeListener} to observe
   * focus change. Only one observable can be used for a view at a time.
   * <p>
   * <em>Note:</em> A value will be emitted immediately on subscribe.
   */
  @CheckResult
  public static Observable<ViewFocusChangeEvent> focusChangeEvents(View view) {
    checkNotNull(view, "view == null");
    return Observable.create(new ViewFocusChangeEventOnSubscribe(view));
  }

  /**
   * Create an observable which emits on {@code view} long-click events. The emitted value is
   * unspecified and should only be used as notification.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link View#setOnLongClickListener} to observe
   * long clicks. Only one observable can be used for a view at a time.
   */
  @CheckResult
  public static Observable<Object> longClicks(View view) {
    checkNotNull(view, "view == null");
    return Observable.create(new ViewLongClickOnSubscribe(view, Functions.FUNC0_ALWAYS_TRUE));
  }

  /**
   * Create an observable which emits on {@code view} long-click events. The emitted value is
   * unspecified and should only be used as notification.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link View#setOnLongClickListener} to observe
   * long clicks. Only one observable can be used for a view at a time.
   *
   * @param handled Function invoked each occurrence to determine the return value of the
   * underlying {@link View.OnLongClickListener}.
   */
  @CheckResult
  public static Observable<Object> longClicks(View view, Func0<Boolean> handled) {
    checkNotNull(view, "view == null");
    checkNotNull(handled, "handled == null");
    return Observable.create(new ViewLongClickOnSubscribe(view, handled));
  }

  /**
   * Create an observable of long-clicks events for {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link View#setOnLongClickListener} to observe
   * long clicks. Only one observable can be used for a view at a time.
   */
  @CheckResult
  public static Observable<ViewLongClickEvent> longClickEvents(View view) {
    checkNotNull(view, "view == null");
    return Observable.create(new ViewLongClickEventOnSubscribe(view, Functions.FUNC1_ALWAYS_TRUE));
  }

  /**
   * Create an observable of long-click events for {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link View#setOnLongClickListener} to observe
   * long clicks. Only one observable can be used for a view at a time.
   *
   * @param handled Function invoked with each value to determine the return value of the
   * underlying {@link View.OnLongClickListener}.
   */
  @CheckResult
  public static Observable<ViewLongClickEvent> longClickEvents(View view,
      Func1<? super ViewLongClickEvent, Boolean> handled) {
    checkNotNull(view, "view == null");
    checkNotNull(handled, "handled == null");
    return Observable.create(new ViewLongClickEventOnSubscribe(view, handled));
  }

  /**
   * Create an observable of touch events for {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link View#setOnTouchListener} to observe
   * touches. Only one observable can be used for a view at a time.
   */
  @CheckResult
  public static Observable<MotionEvent> touches(View view) {
    return touches(view, Functions.FUNC1_ALWAYS_TRUE);
  }

  /**
   * Create an observable of touch events for {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link View#setOnTouchListener} to observe
   * touches. Only one observable can be used for a view at a time.
   *
   * @param handled Function invoked with each value to determine the return value of the
   * underlying {@link View.OnTouchListener}.
   */
  @CheckResult
  public static Observable<MotionEvent> touches(View view,
      Func1<? super MotionEvent, Boolean> handled) {
    return Observable.create(new ViewTouchOnSubscribe(view, handled));
  }

  /**
   * Create an observable of touch events for {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link View#setOnTouchListener} to observe
   * touches. Only one observable can be used for a view at a time.
   */
  @CheckResult
  public static Observable<ViewTouchEvent> touchEvents(View view) {
    return touchEvents(view, Functions.FUNC1_ALWAYS_TRUE);
  }

  /**
   * Create an observable of touch events for {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link View#setOnTouchListener} to observe
   * touches. Only one observable can be used for a view at a time.
   *
   * @param handled Function invoked with each value to determine the return value of the
   * underlying {@link View.OnTouchListener}.
   */
  @CheckResult
  public static Observable<ViewTouchEvent> touchEvents(View view,
      Func1<? super ViewTouchEvent, Boolean> handled) {
    return Observable.create(new ViewTouchEventOnSubscribe(view, handled));
  }

  /**
   * An action which sets the activated property of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult
  public static Action1<? super Boolean> activated(final View view) {
    checkNotNull(view, "view == null");
    return new Action1<Boolean>() {
      @Override public void call(Boolean value) {
        view.setActivated(value);
      }
    };
  }

  /**
   * An action which sets the clickable property of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult
  public static Action1<? super Boolean> clickable(final View view) {
    checkNotNull(view, "view == null");
    return new Action1<Boolean>() {
      @Override public void call(Boolean value) {
        view.setClickable(value);
      }
    };
  }

  /**
   * An action which sets the enabled property of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult
  public static Action1<? super Boolean> enabled(final View view) {
    checkNotNull(view, "view == null");
    return new Action1<Boolean>() {
      @Override public void call(Boolean value) {
        view.setEnabled(value);
      }
    };
  }

  /**
   * An action which sets the pressed property of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult
  public static Action1<? super Boolean> pressed(final View view) {
    checkNotNull(view, "view == null");
    return new Action1<Boolean>() {
      @Override public void call(Boolean value) {
        view.setPressed(value);
      }
    };
  }

  /**
   * An action which sets the selected property of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult
  public static Action1<? super Boolean> selected(final View view) {
    checkNotNull(view, "view == null");
    return new Action1<Boolean>() {
      @Override public void call(Boolean value) {
        view.setSelected(value);
      }
    };
  }

  /**
   * An action which sets the visibility property of {@code view}. {@code false} values use
   * {@code View.GONE}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult
  public static Action1<? super Boolean> visibility(View view) {
    checkNotNull(view, "view == null");
    return visibility(view, View.GONE);
  }

  /**
   * An action which sets the visibility property of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   *
   * @param visibilityWhenFalse Visibility to set on a {@code false} value ({@code View.INVISIBLE}
   * or {@code View.GONE}).
   */
  @CheckResult
  public static Action1<? super Boolean> visibility(final View view,
      final int visibilityWhenFalse) {
    checkNotNull(view, "view == null");
    checkArgument(visibilityWhenFalse != View.VISIBLE,
        "Setting visibility to VISIBLE when false would have no effect.");
    checkArgument(visibilityWhenFalse == View.INVISIBLE || visibilityWhenFalse == View.GONE,
        "Must set visibility to INVISIBLE or GONE when false.");
    return new Action1<Boolean>() {
      @Override public void call(Boolean value) {
        view.setVisibility(value ? View.VISIBLE : visibilityWhenFalse);
      }
    };
  }

  private RxView() {
    throw new AssertionError("No instances.");
  }
}
