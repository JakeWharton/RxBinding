package rx.android.widget

import android.widget.CompoundButton
import rx.Observable
import rx.functions.Action1

/**
 * Create an observable of booleans representing the checked state of {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * <p>
 * <em>Warning:</em> The created observable uses {@link CompoundButton#setOnCheckedChangeListener}
 * to observe checked changes. Only one observable can be used for a view at a time.
 */
public inline fun CompoundButton.checkedChanges(): Observable<Boolean> = RxCompoundButton.checkedChanges(this)

/**
 * Create an observable of checked change events for {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * <p>
 * <em>Warning:</em> The created observable uses {@link CompoundButton#setOnCheckedChangeListener}
 * to observe checked changes. Only one observable can be used for a view at a time.
 */
public inline fun CompoundButton.checkedChangeEvents(): Observable<CompoundButtonCheckedChangeEvent> = RxCompoundButton.checkedChangeEvents(this)

/**
 * An action which sets the checked property of {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
public inline fun CompoundButton.checked(): Action1<in Boolean> = RxCompoundButton.checked(this)

/**
 * An action which sets the toggles property of {@code view} with each value.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
public inline fun CompoundButton.toggle(): Action1<in Any> = RxCompoundButton.toggle(this)
