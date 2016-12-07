package com.jakewharton.rxbinding2.view;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

import java.util.concurrent.Callable;

import static android.os.Build.VERSION_CODES.JELLY_BEAN;
import static android.os.Build.VERSION_CODES.M;
import static com.jakewharton.rxbinding.internal.Preconditions.checkArgument;
import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;
import static com.jakewharton.rxbinding2.internal.Functions.CALLABLE_ALWAYS_TRUE;
import static com.jakewharton.rxbinding2.internal.Functions.PREDICATE_ALWAYS_TRUE;

/**
 * Static factory methods for creating {@linkplain Observable observables} and {@linkplain Consumer
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
  @CheckResult @NonNull
  public static Observable<Object> attaches(@NonNull View view) {
    checkNotNull(view, "view == null");
    return new ViewAttachesObservable(view, true);
  }

  /**
   * Create an observable of attach and detach events on {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Observable<ViewAttachEvent> attachEvents(@NonNull View view) {
    checkNotNull(view, "view == null");
    return new ViewAttachEventObservable(view);
  }

  /**
   * Create an observable which emits on {@code view} detach events. The emitted value is
   * unspecified and should only be used as notification.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Observable<Object> detaches(@NonNull View view) {
    checkNotNull(view, "view == null");
    return new ViewAttachesObservable(view, false);
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
  @CheckResult @NonNull
  public static Observable<Object> clicks(@NonNull View view) {
    checkNotNull(view, "view == null");
    return new ViewClickObservable(view);
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
  @CheckResult @NonNull
  public static Observable<DragEvent> drags(@NonNull View view) {
    checkNotNull(view, "view == null");
    return new ViewDragObservable(view, PREDICATE_ALWAYS_TRUE);
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
   * @param handled Predicate invoked with each value to determine the return value of the
   * underlying {@link View.OnDragListener}.
   */
  @CheckResult @NonNull
  public static Observable<DragEvent> drags(@NonNull View view,
      @NonNull Predicate<? super DragEvent> handled) {
    checkNotNull(view, "view == null");
    checkNotNull(handled, "handled == null");
    return new ViewDragObservable(view, handled);
  }

  /**
   * Create an observable for draws on {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link ViewTreeObserver#addOnDrawListener} to
   * observe draws. Multiple observables can be used for a view at a time.
   */
  @RequiresApi(JELLY_BEAN)
  @CheckResult @NonNull
  public static Observable<Object> draws(@NonNull View view) {
    checkNotNull(view, "view == null");
    return new ViewTreeObserverDrawObservable(view);
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
  @CheckResult @NonNull
  public static Observable<Boolean> focusChanges(@NonNull View view) {
    checkNotNull(view, "view == null");
    return new ViewFocusChangeObservable(view);
  }

  /**
   * Create an observable which emits on {@code view} globalLayout events. The emitted value is
   * unspecified and should only be used as notification.
   * <p></p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link
   * ViewTreeObserver#addOnGlobalLayoutListener} to observe global layouts. Multiple observables
   * can be used for a view at a time.
   */
  @CheckResult @NonNull
  public static Observable<Object> globalLayouts(@NonNull View view) {
    checkNotNull(view, "view == null");
    return new ViewTreeObserverGlobalLayoutObservable(view);
  }

  /**
   * Create an observable of hover events for {@code view}.
   * <p>
   * <em>Warning:</em> Values emitted by this observable are <b>mutable</b> and part of a shared
   * object pool and thus are <b>not safe</b> to cache or delay reading (such as by observing
   * on a different thread). If you want to cache or delay reading the items emitted then you must
   * map values through a function which calls {@link MotionEvent#obtain(MotionEvent)} or
   * {@link MotionEvent#obtainNoHistory(MotionEvent)} to create a copy.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link View#setOnHoverListener} to observe
   * touches. Only one observable can be used for a view at a time.
   */
  @CheckResult @NonNull
  public static Observable<MotionEvent> hovers(@NonNull View view) {
    checkNotNull(view, "view == null");
    return new ViewHoverObservable(view, PREDICATE_ALWAYS_TRUE);
  }

  /**
   * Create an observable of hover events for {@code view}.
   * <p>
   * <em>Warning:</em> Values emitted by this observable are <b>mutable</b> and part of a shared
   * object pool and thus are <b>not safe</b> to cache or delay reading (such as by observing
   * on a different thread). If you want to cache or delay reading the items emitted then you must
   * map values through a function which calls {@link MotionEvent#obtain(MotionEvent)} or
   * {@link MotionEvent#obtainNoHistory(MotionEvent)} to create a copy.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link View#setOnHoverListener} to observe
   * touches. Only one observable can be used for a view at a time.
   *
   * @param handled Predicate invoked with each value to determine the return value of the
   * underlying {@link View.OnHoverListener}.
   */
  @CheckResult @NonNull
  public static Observable<MotionEvent> hovers(@NonNull View view,
      @NonNull Predicate<? super MotionEvent> handled) {
    checkNotNull(view, "view == null");
    checkNotNull(handled, "handled == null");
    return new ViewHoverObservable(view, handled);
  }

  /**
   * Create an observable which emits on {@code view} layout changes. The emitted value is
   * unspecified and should only be used as notification.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Observable<Object> layoutChanges(@NonNull View view) {
    checkNotNull(view, "view == null");
    return new ViewLayoutChangeObservable(view);
  }

  /**
   * Create an observable of layout-change events for {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Observable<ViewLayoutChangeEvent> layoutChangeEvents(@NonNull View view) {
    checkNotNull(view, "view == null");
    return new ViewLayoutChangeEventObservable(view);
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
  @CheckResult @NonNull
  public static Observable<Object> longClicks(@NonNull View view) {
    checkNotNull(view, "view == null");
    return new ViewLongClickObservable(view, CALLABLE_ALWAYS_TRUE);
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
   * @param handled Predicate invoked each occurrence to determine the return value of the
   * underlying {@link View.OnLongClickListener}.
   */
  @CheckResult @NonNull
  public static Observable<Object> longClicks(@NonNull View view,
      @NonNull Callable<Boolean> handled) {
    checkNotNull(view, "view == null");
    checkNotNull(handled, "handled == null");
    return new ViewLongClickObservable(view, handled);
  }

  /**
   * Create an observable for pre-draws on {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link ViewTreeObserver#addOnPreDrawListener} to
   * observe pre-draws. Multiple observables can be used for a view at a time.
   */
  @CheckResult @NonNull
  public static Observable<Object> preDraws(@NonNull View view,
      @NonNull Callable<Boolean> proceedDrawingPass) {
    checkNotNull(view, "view == null");
    checkNotNull(proceedDrawingPass, "proceedDrawingPass == null");
    return new ViewTreeObserverPreDrawObservable(view, proceedDrawingPass);
  }

  /**
   * Create an observable of scroll-change events for {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @RequiresApi(M)
  @CheckResult @NonNull
  public static Observable<ViewScrollChangeEvent> scrollChangeEvents(@NonNull View view) {
    checkNotNull(view, "view == null");
    return new ViewScrollChangeEventObservable(view);
  }

  /**
   * Create an observable of integers representing a new system UI visibility for {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses
   * {@link View#setOnSystemUiVisibilityChangeListener} to observe system UI visibility changes.
   * Only one observable can be used for a view at a time.
   */
  @CheckResult @NonNull
  public static Observable<Integer> systemUiVisibilityChanges(@NonNull View view) {
    checkNotNull(view, "view == null");
    return new ViewSystemUiVisibilityChangeObservable(view);
  }

  /**
   * Create an observable of touch events for {@code view}.
   * <p>
   * <em>Warning:</em> Values emitted by this observable are <b>mutable</b> and part of a shared
   * object pool and thus are <b>not safe</b> to cache or delay reading (such as by observing
   * on a different thread). If you want to cache or delay reading the items emitted then you must
   * map values through a function which calls {@link MotionEvent#obtain(MotionEvent)} or
   * {@link MotionEvent#obtainNoHistory(MotionEvent)} to create a copy.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link View#setOnTouchListener} to observe
   * touches. Only one observable can be used for a view at a time.
   */
  @CheckResult @NonNull
  public static Observable<MotionEvent> touches(@NonNull View view) {
    checkNotNull(view, "view == null");
    return new ViewTouchObservable(view, PREDICATE_ALWAYS_TRUE);
  }

  /**
   * Create an observable of touch events for {@code view}.
   * <p>
   * <em>Warning:</em> Values emitted by this observable are <b>mutable</b> and part of a shared
   * object pool and thus are <b>not safe</b> to cache or delay reading (such as by observing
   * on a different thread). If you want to cache or delay reading the items emitted then you must
   * map values through a function which calls {@link MotionEvent#obtain(MotionEvent)} or
   * {@link MotionEvent#obtainNoHistory(MotionEvent)} to create a copy.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Warning:</em> The created observable uses {@link View#setOnTouchListener} to observe
   * touches. Only one observable can be used for a view at a time.
   *
   * @param handled Predicate invoked with each value to determine the return value of the
   * underlying {@link View.OnTouchListener}.
   */
  @CheckResult @NonNull
  public static Observable<MotionEvent> touches(@NonNull View view,
      @NonNull Predicate<? super MotionEvent> handled) {
    checkNotNull(view, "view == null");
    checkNotNull(handled, "handled == null");
    return new ViewTouchObservable(view, handled);
  }

  /**
   * Create an observable of key events for {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <em>Warning:</em> The created observable uses {@link View#setOnKeyListener} to observe
   * key events. Only one observable can be used for a view at a time.
   */
  @CheckResult @NonNull
  public static Observable<KeyEvent> keys(@NonNull View view) {
    checkNotNull(view, "view == null");
    return new ViewKeyObservable(view, PREDICATE_ALWAYS_TRUE);
  }

  /**
   * Create an observable of key events for {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <em>Warning:</em> The created observable uses {@link View#setOnKeyListener} to observe
   * key events. Only one observable can be used for a view at a time.
   *
   * @param handled Predicate invoked each occurrence to determine the return value of the
   * underlying {@link View.OnKeyListener}.
   */
  @CheckResult @NonNull
  public static Observable<KeyEvent> keys(@NonNull View view,
      @NonNull Predicate<? super KeyEvent> handled) {
    checkNotNull(view, "view == null");
    checkNotNull(handled, "handled == null");
    return new ViewKeyObservable(view, handled);
  }

  /**
   * An action which sets the activated property of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Consumer<? super Boolean> activated(@NonNull final View view) {
    checkNotNull(view, "view == null");
    return new Consumer<Boolean>() {
      @Override public void accept(Boolean value) {
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
  @CheckResult @NonNull
  public static Consumer<? super Boolean> clickable(@NonNull final View view) {
    checkNotNull(view, "view == null");
    return new Consumer<Boolean>() {
      @Override public void accept(Boolean value) {
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
  @CheckResult @NonNull
  public static Consumer<? super Boolean> enabled(@NonNull final View view) {
    checkNotNull(view, "view == null");
    return new Consumer<Boolean>() {
      @Override public void accept(Boolean value) {
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
  @CheckResult @NonNull
  public static Consumer<? super Boolean> pressed(@NonNull final View view) {
    checkNotNull(view, "view == null");
    return new Consumer<Boolean>() {
      @Override public void accept(Boolean value) {
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
  @CheckResult @NonNull
  public static Consumer<? super Boolean> selected(@NonNull final View view) {
    checkNotNull(view, "view == null");
    return new Consumer<Boolean>() {
      @Override public void accept(Boolean value) {
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
  @CheckResult @NonNull
  public static Consumer<? super Boolean> visibility(@NonNull View view) {
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
  @CheckResult @NonNull
  public static Consumer<? super Boolean> visibility(@NonNull final View view,
      final int visibilityWhenFalse) {
    checkNotNull(view, "view == null");
    checkArgument(visibilityWhenFalse != View.VISIBLE,
        "Setting visibility to VISIBLE when false would have no effect.");
    checkArgument(visibilityWhenFalse == View.INVISIBLE || visibilityWhenFalse == View.GONE,
        "Must set visibility to INVISIBLE or GONE when false.");
    return new Consumer<Boolean>() {
      @Override public void accept(Boolean value) {
        view.setVisibility(value ? View.VISIBLE : visibilityWhenFalse);
      }
    };
  }

  private RxView() {
    throw new AssertionError("No instances.");
  }
}
