package rx.android.widget

import android.widget.TextView
import rx.Observable
import rx.functions.Action1

/**
 * Create an observable of character sequences for text changes on {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun TextView.textChanges(): Observable<CharSequence> = RxTextView.textChanges(this)

/**
 * Create an observable of text change events for {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun TextView.textChangeEvents(): Observable<TextViewTextChangeEvent> = RxTextView.textChangeEvents(this)

/**
 * An action which sets the text property of {@code view} with character sequences.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun TextView.text(): Action1<in CharSequence> = RxTextView.setText(this)

/**
 * An action which sets the text property of {@code view} string resource IDs.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun TextView.textRes(): Action1<in Int> = RxTextView.setTextRes(this)
