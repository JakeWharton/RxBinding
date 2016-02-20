package com.jakewharton.rxbinding.widget

import android.widget.TextView
import com.jakewharton.rxbinding.internal.Functions
import rx.Observable
import rx.functions.Action1
import rx.functions.Func1

/**
 * Create an observable of editor actions on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [TextView.OnEditorActionListener] to
 * observe actions. Only one observable can be used for a view at a time.
 */
public inline fun TextView.editorActions(): Observable<Int> = RxTextView.editorActions(this)

/**
 * Create an observable of editor actions on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [TextView.OnEditorActionListener] to
 * observe actions. Only one observable can be used for a view at a time.
 *
 * @param handled Function invoked each occurrence to determine the return value of the
 * underlying [TextView.OnEditorActionListener].
 */
public inline fun TextView.editorActions(handled: Func1<in Int, Boolean>): Observable<Int> = RxTextView.editorActions(this, handled)

/**
 * Create an observable of editor action events on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [TextView.OnEditorActionListener] to
 * observe actions. Only one observable can be used for a view at a time.
 */
public inline fun TextView.editorActionEvents(): Observable<TextViewEditorActionEvent> = RxTextView.editorActionEvents(this)

/**
 * Create an observable of editor action events on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [TextView.OnEditorActionListener] to
 * observe actions. Only one observable can be used for a view at a time.
 *
 * @param handled Function invoked each occurrence to determine the return value of the
 * underlying [TextView.OnEditorActionListener].
 */
public inline fun TextView.editorActionEvents(handled: Func1<in TextViewEditorActionEvent, Boolean>): Observable<TextViewEditorActionEvent> = RxTextView.editorActionEvents(this, handled)

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
public inline fun TextView.textChanges(): Observable<CharSequence> = RxTextView.textChanges(this)

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
public inline fun TextView.textChangeEvents(): Observable<TextViewTextChangeEvent> = RxTextView.textChangeEvents(this)

/**
 * Create an observable of before text change events for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
public inline fun TextView.beforeTextChangeEvents(): Observable<TextViewBeforeTextChangeEvent> = RxTextView.beforeTextChangeEvents(this)

/**
 * Create an observable of after text change events for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
public inline fun TextView.afterTextChangeEvents(): Observable<TextViewAfterTextChangeEvent> = RxTextView.afterTextChangeEvents(this)

/**
 * An action which sets the text property of `view` with character sequences.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun TextView.text(): Action1<in CharSequence> = RxTextView.text(this)

/**
 * An action which sets the text property of `view` string resource IDs.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun TextView.textRes(): Action1<in Int> = RxTextView.textRes(this)

/**
 * An action which sets the error property of `view` with character sequences.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun TextView.error(): Action1<in CharSequence> = RxTextView.error(this)

/**
 * An action which sets the error property of `view` string resource IDs.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun TextView.errorRes(): Action1<in Int> = RxTextView.errorRes(this)

/**
 * An action which sets the hint property of `view` with character sequences.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun TextView.hint(): Action1<in CharSequence> = RxTextView.hint(this)

/**
 * An action which sets the hint property of `view` string resource IDs.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun TextView.hintRes(): Action1<in Int> = RxTextView.hintRes(this)

/**
 * An action which sets the color property of `view` with color integer.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun TextView.color(): Action1<in Int> = RxTextView.color(this)
