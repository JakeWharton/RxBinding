package rx.android.view;

import android.view.DragEvent;
import android.view.View;
import rx.Observable;
import rx.android.internal.Functions;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;

import static rx.android.internal.Preconditions.checkArgument;
import static rx.android.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables} and {@linkplain Action1
 * actions} for {@link View}.
 */
public final class RxView {
  /**
   * Create an observable of timestamps for clicks on {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link View#setOnClickListener} to observe
   * clicks. Only one observable can be used for a view at a time.
   */
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
   */
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
   */
  public static Observable<ViewFocusChangeEvent> focusChangeEvents(View view) {
    checkNotNull(view, "view == null");
    return Observable.create(new ViewFocusChangeEventOnSubscribe(view));
  }

  /**
   * Create an observable of timestamps for long-clicks on {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link View#setOnLongClickListener} to observe
   * long clicks. Only one observable can be used for a view at a time.
   */
  public static Observable<Object> longClicks(View view) {
    checkNotNull(view, "view == null");
    return Observable.create(new ViewLongClickOnSubscribe(view, Functions.FUNC0_ALWAYS_TRUE));
  }

  /**
   * Create an observable of timestamps for clicks on {@code view}.
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
  public static Observable<ViewLongClickEvent> longClickEvents(View view,
      Func1<? super ViewLongClickEvent, Boolean> handled) {
    checkNotNull(view, "view == null");
    checkNotNull(handled, "handled == null");
    return Observable.create(new ViewLongClickEventOnSubscribe(view, handled));
  }

  /**
   * An action which sets the activated property of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  public static Action1<? super Boolean> setActivated(final View view) {
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
  public static Action1<? super Boolean> setClickable(final View view) {
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
  public static Action1<? super Boolean> setEnabled(final View view) {
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
  public static Action1<? super Boolean> setPressed(final View view) {
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
  public static Action1<? super Boolean> setSelected(final View view) {
    checkNotNull(view, "view == null");
    return new Action1<Boolean>() {
      @Override public void call(Boolean value) {
        view.setSelected(value);
      }
    };
  }

  /**
   * An action which sets the visibility property of {@code view}. {@code false} values use
   * {@link View#GONE View.GONE}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  public static Action1<? super Boolean> setVisibility(View view) {
    checkNotNull(view, "view == null");
    return setVisibility(view, View.GONE);
  }

  /**
   * An action which sets the visibility property of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   *
   * @param visibilityWhenFalse Visibility to set on a {@code false} value ({@link View#INVISIBLE
   * View.INVISIBLE} or {@link View#GONE View.GONE}).
   */
  public static Action1<? super Boolean> setVisibility(final View view,
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
