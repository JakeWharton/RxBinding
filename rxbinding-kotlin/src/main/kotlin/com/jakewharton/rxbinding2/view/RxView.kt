package com.jakewharton.rxbinding2.view

import android.view.DragEvent
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Predicate
import java.util.concurrent.Callable

/**
 * Create an observable which emits on `view` attach events. The emitted value is
 * unspecified and should only be used as notification.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun View.attaches(): Observable<Any> = RxView.attaches(this)

/**
 * Create an observable of attach and detach events on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun View.attachEvents(): Observable<ViewAttachEvent> = RxView.attachEvents(this)

/**
 * Create an observable which emits on `view` detach events. The emitted value is
 * unspecified and should only be used as notification.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun View.detaches(): Observable<Any> = RxView.detaches(this)

/**
 * Create an observable which emits on `view` click events. The emitted value is
 * unspecified and should only be used as notification.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [View.setOnClickListener] to observe
 * clicks. Only one observable can be used for a view at a time.
 */
inline fun View.clicks(): Observable<Any> = RxView.clicks(this)

/**
 * Create an observable of [DragEvent] for drags on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [View.setOnDragListener] to observe
 * drags. Only one observable can be used for a view at a time.
 */
inline fun View.drags(): Observable<DragEvent> = RxView.drags(this)

/**
 * Create an observable of [DragEvent] for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [View.setOnDragListener] to observe
 * drags. Only one observable can be used for a view at a time.
 *
 * @param handled Predicate invoked with each value to determine the return value of the
 * underlying [View.OnDragListener].
 */
inline fun View.drags(handled: Predicate<in DragEvent>): Observable<DragEvent> = RxView.drags(this, handled)

/**
 * Create an observable for draws on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [ViewTreeObserver.addOnDrawListener] to
 * observe draws. Multiple observables can be used for a view at a time.
 */
inline fun View.draws(): Observable<Any> = RxView.draws(this)

/**
 * Create an observable of booleans representing the focus of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [View.setOnFocusChangeListener] to observe
 * focus change. Only one observable can be used for a view at a time.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
inline fun View.focusChanges(): Observable<Boolean> = RxView.focusChanges(this)

/**
 * Create an observable which emits on `view` globalLayout events. The emitted value is
 * unspecified and should only be used as notification.
 * </p>
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses {@link
 * ViewTreeObserver#addOnGlobalLayoutListener} to observe global layouts. Multiple observables
 * can be used for a view at a time.
 */
inline fun View.globalLayouts(): Observable<Any> = RxView.globalLayouts(this)

/**
 * Create an observable of hover events for `view`.
 *
 * *Warning:* Values emitted by this observable are <b>mutable</b> and part of a shared
 * object pool and thus are <b>not safe</b> to cache or delay reading (such as by observing
 * on a different thread). If you want to cache or delay reading the items emitted then you must
 * map values through a function which calls {@link MotionEvent#obtain(MotionEvent)} or
 * {@link MotionEvent#obtainNoHistory(MotionEvent)} to create a copy.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [View.setOnHoverListener] to observe
 * touches. Only one observable can be used for a view at a time.
 */
inline fun View.hovers(): Observable<MotionEvent> = RxView.hovers(this)

/**
 * Create an observable of hover events for `view`.
 *
 * *Warning:* Values emitted by this observable are <b>mutable</b> and part of a shared
 * object pool and thus are <b>not safe</b> to cache or delay reading (such as by observing
 * on a different thread). If you want to cache or delay reading the items emitted then you must
 * map values through a function which calls {@link MotionEvent#obtain(MotionEvent)} or
 * {@link MotionEvent#obtainNoHistory(MotionEvent)} to create a copy.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [View.setOnHoverListener] to observe
 * touches. Only one observable can be used for a view at a time.
 *
 * @param handled Predicate invoked with each value to determine the return value of the
 * underlying [View.OnHoverListener].
 */
inline fun View.hovers(handled: Predicate<in MotionEvent>): Observable<MotionEvent> = RxView.hovers(this, handled)

/**
 * Create an observable which emits on `view` layout changes. The emitted value is
 * unspecified and should only be used as notification.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun View.layoutChanges(): Observable<Any> = RxView.layoutChanges(this)

/**
 * Create an observable of layout-change events for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun View.layoutChangeEvents(): Observable<ViewLayoutChangeEvent> = RxView.layoutChangeEvents(this)

/**
 * Create an observable which emits on `view` long-click events. The emitted value is
 * unspecified and should only be used as notification.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [View.setOnLongClickListener] to observe
 * long clicks. Only one observable can be used for a view at a time.
 */
inline fun View.longClicks(): Observable<Any> = RxView.longClicks(this)

/**
 * Create an observable which emits on `view` long-click events. The emitted value is
 * unspecified and should only be used as notification.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [View.setOnLongClickListener] to observe
 * long clicks. Only one observable can be used for a view at a time.
 *
 * @param handled Predicate invoked each occurrence to determine the return value of the
 * underlying [View.OnLongClickListener].
 */
inline fun View.longClicks(handled: Callable<Boolean>): Observable<Any> = RxView.longClicks(this, handled)

/**
 * Create an observable for pre-draws on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [ViewTreeObserver.addOnPreDrawListener] to
 * observe pre-draws. Multiple observables can be used for a view at a time.
 */
inline fun View.preDraws(proceedDrawingPass: Callable<Boolean>): Observable<Any> = RxView.preDraws(this, proceedDrawingPass)

/**
 * Create an observable of scroll-change events for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun View.scrollChangeEvents(): Observable<ViewScrollChangeEvent> = RxView.scrollChangeEvents(this)

/**
 * Create an observable of integers representing a new system UI visibility for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses
 * [View.setOnSystemUiVisibilityChangeListener] to observe system UI visibility changes.
 * Only one observable can be used for a view at a time.
 */
inline fun View.systemUiVisibilityChanges(): Observable<Int> = RxView.systemUiVisibilityChanges(this)

/**
 * Create an observable of touch events for `view`.
 *
 * *Warning:* Values emitted by this observable are <b>mutable</b> and part of a shared
 * object pool and thus are <b>not safe</b> to cache or delay reading (such as by observing
 * on a different thread). If you want to cache or delay reading the items emitted then you must
 * map values through a function which calls {@link MotionEvent#obtain(MotionEvent)} or
 * {@link MotionEvent#obtainNoHistory(MotionEvent)} to create a copy.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [View.setOnTouchListener] to observe
 * touches. Only one observable can be used for a view at a time.
 */
inline fun View.touches(): Observable<MotionEvent> = RxView.touches(this)

/**
 * Create an observable of touch events for `view`.
 *
 * *Warning:* Values emitted by this observable are <b>mutable</b> and part of a shared
 * object pool and thus are <b>not safe</b> to cache or delay reading (such as by observing
 * on a different thread). If you want to cache or delay reading the items emitted then you must
 * map values through a function which calls {@link MotionEvent#obtain(MotionEvent)} or
 * {@link MotionEvent#obtainNoHistory(MotionEvent)} to create a copy.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [View.setOnTouchListener] to observe
 * touches. Only one observable can be used for a view at a time.
 *
 * @param handled Predicate invoked with each value to determine the return value of the
 * underlying [View.OnTouchListener].
 */
inline fun View.touches(handled: Predicate<in MotionEvent>): Observable<MotionEvent> = RxView.touches(this, handled)

/**
 * Create an observable of key events for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 * *Warning:* The created observable uses [View.setOnKeyListener] to observe
 * key events. Only one observable can be used for a view at a time.
 */
inline fun View.keys(): Observable<KeyEvent> = RxView.keys(this)

/**
 * Create an observable of key events for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 * *Warning:* The created observable uses [View.setOnKeyListener] to observe
 * key events. Only one observable can be used for a view at a time.
 *
 * @param handled Predicate invoked each occurrence to determine the return value of the
 * underlying [View.OnKeyListener].
 */
inline fun View.keys(handled: Predicate<in KeyEvent>): Observable<KeyEvent> = RxView.keys(this, handled)

/**
 * An action which sets the activated property of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun View.activated(): Consumer<in Boolean> = RxView.activated(this)

/**
 * An action which sets the clickable property of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun View.clickable(): Consumer<in Boolean> = RxView.clickable(this)

/**
 * An action which sets the enabled property of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun View.enabled(): Consumer<in Boolean> = RxView.enabled(this)

/**
 * An action which sets the pressed property of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun View.pressed(): Consumer<in Boolean> = RxView.pressed(this)

/**
 * An action which sets the selected property of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun View.selected(): Consumer<in Boolean> = RxView.selected(this)

/**
 * An action which sets the visibility property of `view`. `false` values use
 * `View.GONE`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun View.visibility(): Consumer<in Boolean> = RxView.visibility(this)

/**
 * An action which sets the visibility property of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * @param visibilityWhenFalse Visibility to set on a `false` value (`View.INVISIBLE`
 * or `View.GONE`).
 */
inline fun View.visibility(visibilityWhenFalse: Int): Consumer<in Boolean> = RxView.visibility(this, visibilityWhenFalse)
