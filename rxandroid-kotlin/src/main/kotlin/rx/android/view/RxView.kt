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
fun View.clicks(view: View): Observable<Any> = RxView.clicks(view)

/**
 * Create an observable of click events for {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * <p>
 * <em>Warning:</em> The created observable uses {@link View#setOnClickListener} to observe
 * clicks. Only one observable can be used for a view at a time.
 */
fun View.clickEvents(view: View): Observable<ViewClickEvent> = RxView.clickEvents(view)

/**
 * Create an observable of {@link DragEvent} for drags on {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * <p>
 * <em>Warning:</em> The created observable uses {@link View#setOnDragListener} to observe
 * drags. Only one observable can be used for a view at a time.
 */
fun View.drags(view: View): Observable<DragEvent> = RxView.drags(view)
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
fun View.dragEvents(view: View): Observable<ViewDragEvent> = RxView.dragEvents(view)
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
fun View.focusChanges(view: View): Observable<Boolean> = RxView.focusChanges(view)

/**
 * Create an observable of focus-change events for {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * <p>
 * <em>Warning:</em> The created observable uses {@link View#setOnFocusChangeListener} to observe
 * focus change. Only one observable can be used for a view at a time.
 */
fun View.focusChangeEvents(view: View): Observable<ViewFocusChangeEvent> = RxView.focusChangeEvents(view)

/**
 * Create an observable of timestamps for long-clicks on {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * <p>
 * <em>Warning:</em> The created observable uses {@link View#setOnLongClickListener} to observe
 * long clicks. Only one observable can be used for a view at a time.
 */
fun View.longClicks(view: View): Observable<Any> = RxView.longClicks(view)
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
fun View.longClickEvents(view: View): Observable<ViewLongClickEvent> = RxView.longClickEvents(view)
// TODO overload with Func

/**
 * An action which sets the activated property of {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun View.activated(view: View): Action1<in Boolean> = RxView.setActivated(view)

/**
 * An action which sets the clickable property of {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun View.clickable(view: View): Action1<in Boolean> = RxView.setClickable(view)

/**
 * An action which sets the enabled property of {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun View.enabled(view: View): Action1<in Boolean> = RxView.setEnabled(view)

/**
 * An action which sets the pressed property of {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun View.pressed(view: View): Action1<in Boolean> = RxView.setPressed(view)

/**
 * An action which sets the selected property of {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun View.selected(view: View): Action1<in Boolean> = RxView.setSelected(view)

/**
 * An action which sets the visibility property of {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 *
 * @param visibilityWhenFalse Visibility to set on a {@code false} value ({@link View#INVISIBLE
 * View.INVISIBLE} or {@link View#GONE View.GONE}).
 */
fun View.visibility(view: View, visibilityWhenFalse: Int = View.GONE): Action1<in Boolean> {
  return RxView.setVisibility(view, visibilityWhenFalse)
}
