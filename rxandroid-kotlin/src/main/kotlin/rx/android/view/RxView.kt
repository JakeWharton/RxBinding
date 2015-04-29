package rx.android.view

import android.view.DragEvent
import android.view.View
import rx.Observable
import rx.functions.Action1

/**
 * Create an observable of timestamps for clicks on {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * <p>
 * <em>Warning:</em> The created observable uses {@link View#setOnClickListener} to observe
 * clicks. Only one observable can be used for a view at a time.
 */
fun View.clicks(): Observable<Any> = RxView.clicks(this)

/**
 * Create an observable of click events for {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * <p>
 * <em>Warning:</em> The created observable uses {@link View#setOnClickListener} to observe
 * clicks. Only one observable can be used for a view at a time.
 */
fun View.clickEvents(): Observable<ViewClickEvent> = RxView.clickEvents(this)

/**
 * Create an observable of {@link DragEvent} for drags on {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * <p>
 * <em>Warning:</em> The created observable uses {@link View#setOnDragListener} to observe
 * drags. Only one observable can be used for a view at a time.
 */
fun View.drags(): Observable<DragEvent> = RxView.drags(this)
// TODO overload with Func

/**
 * Create an observable of drag events for {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * <p>
 * <em>Warning:</em> The created observable uses {@link View#setOnDragListener} to observe
 * drags. Only one observable can be used for a view at a time.
 */
fun View.dragEvents(): Observable<ViewDragEvent> = RxView.dragEvents(this)
// TODO overload with Func

/**
 * Create an observable of booleans representing the focus of {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * <p>
 * <em>Warning:</em> The created observable uses {@link View#setOnFocusChangeListener} to observe
 * focus change. Only one observable can be used for a view at a time.
 */
fun View.focusChanges(): Observable<Boolean> = RxView.focusChanges(this)

/**
 * Create an observable of focus-change events for {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * <p>
 * <em>Warning:</em> The created observable uses {@link View#setOnFocusChangeListener} to observe
 * focus change. Only one observable can be used for a view at a time.
 */
fun View.focusChangeEvents(): Observable<ViewFocusChangeEvent> = RxView.focusChangeEvents(this)

/**
 * Create an observable of timestamps for long-clicks on {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * <p>
 * <em>Warning:</em> The created observable uses {@link View#setOnLongClickListener} to observe
 * long clicks. Only one observable can be used for a view at a time.
 */
fun View.longClicks(): Observable<Any> = RxView.longClicks(this)
// TODO overload with Func

/**
 * Create an observable of long-clicks events for {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * <p>
 * <em>Warning:</em> The created observable uses {@link View#setOnLongClickListener} to observe
 * long clicks. Only one observable can be used for a view at a time.
 */
fun View.longClickEvents(): Observable<ViewLongClickEvent> = RxView.longClickEvents(this)
// TODO overload with Func

/**
 * An action which sets the activated property of {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun View.activated(): Action1<in Boolean> = RxView.setActivated(this)

/**
 * An action which sets the clickable property of {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun View.clickable(): Action1<in Boolean> = RxView.setClickable(this)

/**
 * An action which sets the enabled property of {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun View.enabled(): Action1<in Boolean> = RxView.setEnabled(this)

/**
 * An action which sets the pressed property of {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun View.pressed(): Action1<in Boolean> = RxView.setPressed(this)

/**
 * An action which sets the selected property of {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun View.selected(): Action1<in Boolean> = RxView.setSelected(this)

/**
 * An action which sets the visibility property of {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 *
 * @param visibilityWhenFalse Visibility to set on a {@code false} value ({@link View#INVISIBLE
 * View.INVISIBLE} or {@link View#GONE View.GONE}).
 */
fun View.visibility(visibilityWhenFalse: Int = View.GONE): Action1<in Boolean> {
  return RxView.setVisibility(this, visibilityWhenFalse)
}
