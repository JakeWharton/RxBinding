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
fun CompoundButton.checkedChanges(view: CompoundButton): Observable<Boolean> = RxCompoundButton.checkedChanges(view)

/**
 * Create an observable of checked change events for {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * <p>
 * <em>Warning:</em> The created observable uses {@link CompoundButton#setOnCheckedChangeListener}
 * to observe checked changes. Only one observable can be used for a view at a time.
 */
fun CompoundButton.checkedChangeEvents(view: CompoundButton): Observable<CompoundButtonCheckedChangeEvent> = RxCompoundButton.checkedChangeEvents(view)

/**
 * An action which sets the checked property of {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun CompoundButton.checked(view: CompoundButton): Action1<in Boolean> = RxCompoundButton.setChecked(view)

/**
 * An action which sets the toggles property of {@code view} with each value.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun CompoundButton.toggle(view: CompoundButton): Action1<in Any> = RxCompoundButton.toggle(view)
