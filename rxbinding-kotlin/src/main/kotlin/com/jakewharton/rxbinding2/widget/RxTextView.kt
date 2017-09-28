@file:Suppress(
    names = "NOTHING_TO_INLINE"
)

package com.jakewharton.rxbinding2.widget

import android.support.annotation.CheckResult
import android.widget.TextView
import com.jakewharton.rxbinding2.InitialValueObservable
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Predicate

/**
 * Create an observable of editor actions on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [TextView.OnEditorActionListener] to
 * observe actions. Only one observable can be used for a view at a time.
 */
@CheckResult
inline fun TextView.editorActions(): Observable<Int> = RxTextView.editorActions(this)

/**
 * Create an observable of editor actions on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [TextView.OnEditorActionListener] to
 * observe actions. Only one observable can be used for a view at a time.
 *
 * @param handled Predicate invoked each occurrence to determine the return value of the
 * underlying [TextView.OnEditorActionListener].
 */
@CheckResult
inline fun TextView.editorActions(handled: Predicate<in Int>): Observable<Int> = RxTextView.editorActions(this, handled)

/**
 * Create an observable of editor action events on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [TextView.OnEditorActionListener] to
 * observe actions. Only one observable can be used for a view at a time.
 */
@CheckResult
inline fun TextView.editorActionEvents(): Observable<TextViewEditorActionEvent> = RxTextView.editorActionEvents(this)

/**
 * Create an observable of editor action events on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [TextView.OnEditorActionListener] to
 * observe actions. Only one observable can be used for a view at a time.
 *
 * @param handled Predicate invoked each occurrence to determine the return value of the
 * underlying [TextView.OnEditorActionListener].
 */
@CheckResult
inline fun TextView.editorActionEvents(handled: Predicate<in TextViewEditorActionEvent>): Observable<TextViewEditorActionEvent> = RxTextView.editorActionEvents(this, handled)

/**
 * Create an observable of character sequences for text changes on `view`.
 *
 * *Warning:* Values emitted by this observable are <b>mutable</b> and owned by the host
 * `TextView` and thus are <b>not safe</b> to cache or delay reading (such as by observing
 * on a different thread). If you want to cache or delay reading the items emitted then you must
 * map values through a function which calls [String.valueOf] or
 * {@link CharSequence#toString() .toString()} to create a copy.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
inline fun TextView.textChanges(): InitialValueObservable<CharSequence> = RxTextView.textChanges(this)

/**
 * Create an observable of text change events for `view`.
 *
 * *Warning:* Values emitted by this observable contain a <b>mutable</b>
 * [CharSequence] owned by the host `TextView` and thus are <b>not safe</b> to cache
 * or delay reading (such as by observing on a different thread). If you want to cache or delay
 * reading the items emitted then you must map values through a function which calls
 * [String.valueOf] or {@link CharSequence#toString() .toString()} to create a copy.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
inline fun TextView.textChangeEvents(): InitialValueObservable<TextViewTextChangeEvent> = RxTextView.textChangeEvents(this)

/**
 * Create an observable of before text change events for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
inline fun TextView.beforeTextChangeEvents(): InitialValueObservable<TextViewBeforeTextChangeEvent> = RxTextView.beforeTextChangeEvents(this)

/**
 * Create an observable of after text change events for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe using
 * {@link TextView#getEditableText()}.
 */
@CheckResult
inline fun TextView.afterTextChangeEvents(): InitialValueObservable<TextViewAfterTextChangeEvent> = RxTextView.afterTextChangeEvents(this)

/**
 * An action which sets the text property of `view` with character sequences.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
inline fun TextView.text(): Consumer<in CharSequence> = RxTextView.text(this)

/**
 * An action which sets the text property of `view` string resource IDs.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
inline fun TextView.textRes(): Consumer<in Int> = RxTextView.textRes(this)

/**
 * An action which sets the error property of `view` with character sequences.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
inline fun TextView.error(): Consumer<in CharSequence> = RxTextView.error(this)

/**
 * An action which sets the error property of `view` string resource IDs.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
inline fun TextView.errorRes(): Consumer<in Int> = RxTextView.errorRes(this)

/**
 * An action which sets the hint property of `view` with character sequences.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
inline fun TextView.hint(): Consumer<in CharSequence> = RxTextView.hint(this)

/**
 * An action which sets the hint property of `view` string resource IDs.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
inline fun TextView.hintRes(): Consumer<in Int> = RxTextView.hintRes(this)

/**
 * An action which sets the color property of `view` with color integer.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
inline fun TextView.color(): Consumer<in Int> = RxTextView.color(this)
