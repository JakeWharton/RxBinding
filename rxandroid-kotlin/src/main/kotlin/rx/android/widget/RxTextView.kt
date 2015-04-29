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
fun TextView.textChanges(view: TextView): Observable<CharSequence> = RxTextView.textChanges(view)

/**
 * Create an observable of text change events for {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun TextView.textChangeEvents(view: TextView): Observable<TextViewTextChangeEvent> = RxTextView.textChangeEvents(view)

/**
 * An action which sets the text property of {@code view} with character sequences.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun TextView.text(view: TextView): Action1<in CharSequence> = RxTextView.setText(view)

/**
 * An action which sets the text property of {@code view} string resource IDs.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
fun TextView.textRes(view: TextView): Action1<in Int> = RxTextView.setTextRes(view)
