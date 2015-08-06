package com.jakewharton.rxbinding.view

import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import rx.Observable
import com.jakewharton.rxbinding.internal.Functions
import rx.functions.Action1
import rx.functions.Func0
import rx.functions.Func1

/**
 * Create an observable of timestamps for clicks on {@code view}.
 * 
 * *Warning:* The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * 
 * *Warning:* The created observable uses {@link View#setOnClickListener} to observe
 * clicks. Only one observable can be used for a view at a time.
 */
public inline fun View.clicks(): Observable<Any> = RxView.clicks(this)

/**
 * Create an observable of click events for {@code view}.
 * 
 * *Warning:* The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * 
 * *Warning:* The created observable uses {@link View#setOnClickListener} to observe
 * clicks. Only one observable can be used for a view at a time.
 */
public inline fun View.clickEvents(): Observable<ViewClickEvent> = RxView.clickEvents(this)

/**
 * Create an observable of {@link DragEvent} for drags on {@code view}.
 * 
 * *Warning:* The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * 
 * *Warning:* The created observable uses {@link View#setOnDragListener} to observe
 * drags. Only one observable can be used for a view at a time.
 */
public inline fun View.drags(): Observable<DragEvent> = RxView.drags(this)

/**
 * Create an observable of {@link DragEvent} for {@code view}.
 * 
 * *Warning:* The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * 
 * *Warning:* The created observable uses {@link View#setOnDragListener} to observe
 * drags. Only one observable can be used for a view at a time.
 *
 * @param handled Function invoked with each value to determine the return value of the
 * underlying {@link View.OnDragListener}.
 */
public inline fun View.drags(handled: Func1<DragEvent, Boolean>): Observable<DragEvent> = RxView.drags(this, handled)

/**
 * Create an observable of drag events for {@code view}.
 * 
 * *Warning:* The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * 
 * *Warning:* The created observable uses {@link View#setOnDragListener} to observe
 * drags. Only one observable can be used for a view at a time.
 */
public inline fun View.dragEvents(): Observable<ViewDragEvent> = RxView.dragEvents(this)

/**
 * Create an observable of drag events for {@code view}.
 * 
 * *Warning:* The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * 
 * *Warning:* The created observable uses {@link View#setOnDragListener} to observe
 * drags. Only one observable can be used for a view at a time.
 *
 * @param handled Function invoked with each value to determine the return value of the
 * underlying {@link View.OnDragListener}.
 */
public inline fun View.dragEvents(handled: Func1<ViewDragEvent, Boolean>): Observable<ViewDragEvent> = RxView.dragEvents(this, handled)

/**
 * Create an observable of booleans representing the focus of {@code view}.
 * 
 * *Warning:* The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * 
 * *Warning:* The created observable uses {@link View#setOnFocusChangeListener} to observe
 * focus change. Only one observable can be used for a view at a time.
 */
public inline fun View.focusChanges(): Observable<Boolean> = RxView.focusChanges(this)

/**
 * Create an observable of focus-change events for {@code view}.
 * 
 * *Warning:* The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * 
 * *Warning:* The created observable uses {@link View#setOnFocusChangeListener} to observe
 * focus change. Only one observable can be used for a view at a time.
 */
public inline fun View.focusChangeEvents(): Observable<ViewFocusChangeEvent> = RxView.focusChangeEvents(this)

/**
 * Create an observable of timestamps for long-clicks on {@code view}.
 * 
 * *Warning:* The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * 
 * *Warning:* The created observable uses {@link View#setOnLongClickListener} to observe
 * long clicks. Only one observable can be used for a view at a time.
 */
public inline fun View.longClicks(): Observable<Any> = RxView.longClicks(this)

/**
 * Create an observable of timestamps for clicks on {@code view}.
 * 
 * *Warning:* The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * 
 * *Warning:* The created observable uses {@link View#setOnLongClickListener} to observe
 * long clicks. Only one observable can be used for a view at a time.
 *
 * @param handled Function invoked each occurrence to determine the return value of the
 * underlying {@link View.OnLongClickListener}.
 */
public inline fun View.longClicks(handled: Func0<Boolean>): Observable<Any> = RxView.longClicks(this, handled)

/**
 * Create an observable of long-clicks events for {@code view}.
 * 
 * *Warning:* The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * 
 * *Warning:* The created observable uses {@link View#setOnLongClickListener} to observe
 * long clicks. Only one observable can be used for a view at a time.
 */
public inline fun View.longClickEvents(): Observable<ViewLongClickEvent> = RxView.longClickEvents(this)

/**
 * Create an observable of long-click events for {@code view}.
 * 
 * *Warning:* The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * 
 * *Warning:* The created observable uses {@link View#setOnLongClickListener} to observe
 * long clicks. Only one observable can be used for a view at a time.
 *
 * @param handled Function invoked with each value to determine the return value of the
 * underlying {@link View.OnLongClickListener}.
 */
public inline fun View.longClickEvents(handled: Func1<in ViewLongClickEvent, Boolean>): Observable<ViewLongClickEvent> = RxView.longClickEvents(this, handled)

/**
 * Create an observable of touch events for {@code view}.
 * 
 * *Warning:* The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * 
 * *Warning:* The created observable uses {@link View#setOnTouchListener} to observe
 * touches. Only one observable can be used for a view at a time.
 */
public inline fun View.touches(): Observable<MotionEvent> = RxView.touches(this)

/**
 * Create an observable of touch events for {@code view}.
 * 
 * *Warning:* The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * 
 * *Warning:* The created observable uses {@link View#setOnTouchListener} to observe
 * touches. Only one observable can be used for a view at a time.
 *
 * @param handled Function invoked with each value to determine the return value of the
 * underlying {@link View.OnTouchListener}.
 */
public inline fun View.touches(handled: Func1<in MotionEvent, Boolean>): Observable<MotionEvent> = RxView.touches(this, handled)

/**
 * Create an observable of touch events for {@code view}.
 * 
 * *Warning:* The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * 
 * *Warning:* The created observable uses {@link View#setOnTouchListener} to observe
 * touches. Only one observable can be used for a view at a time.
 */
public inline fun View.touchEvents(): Observable<ViewTouchEvent> = RxView.touchEvents(this)

/**
 * Create an observable of touch events for {@code view}.
 * 
 * *Warning:* The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * 
 * *Warning:* The created observable uses {@link View#setOnTouchListener} to observe
 * touches. Only one observable can be used for a view at a time.
 *
 * @param handled Function invoked with each value to determine the return value of the
 * underlying {@link View.OnTouchListener}.
 */
public inline fun View.touchEvents(handled: Func1<in ViewTouchEvent, Boolean>): Observable<ViewTouchEvent> = RxView.touchEvents(this, handled)

/**
 * An action which sets the activated property of {@code view}.
 * 
 * *Warning:* The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
public inline fun View.activated(): Action1<in Boolean> = RxView.activated(this)

/**
 * An action which sets the clickable property of {@code view}.
 * 
 * *Warning:* The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
public inline fun View.clickable(): Action1<in Boolean> = RxView.clickable(this)

/**
 * An action which sets the enabled property of {@code view}.
 * 
 * *Warning:* The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
public inline fun View.enabled(): Action1<in Boolean> = RxView.enabled(this)

/**
 * An action which sets the pressed property of {@code view}.
 * 
 * *Warning:* The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
public inline fun View.pressed(): Action1<in Boolean> = RxView.pressed(this)

/**
 * An action which sets the selected property of {@code view}.
 * 
 * *Warning:* The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
public inline fun View.selected(): Action1<in Boolean> = RxView.selected(this)

/**
 * An action which sets the visibility property of {@code view}. {@code false} values use
 * {@code View.GONE}.
 *
 * *Warning:* The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
public inline fun View.visibility(): Action1<in Boolean> = RxView.visibility(this)

/**
 * An action which sets the visibility property of {@code view}.
 * 
 * *Warning:* The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 *
 * @param visibilityWhenFalse Visibility to set on a {@code false} value ({@code View.INVISIBLE}
 * or {@code View.GONE}).
 */
public inline fun View.visibility(visibilityWhenFalse: Int): Action1<in Boolean> = RxView.visibility(this, visibilityWhenFalse)

