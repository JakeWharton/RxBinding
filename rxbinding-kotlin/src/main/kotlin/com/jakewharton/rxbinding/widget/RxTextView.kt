package com.jakewharton.rxbinding.widget

import android.widget.TextView
import com.jakewharton.rxbinding.internal.Functions
import rx.Observable
import rx.functions.Action1
import rx.functions.Func1

/**
 * Create an observable of editor actions on {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * <p>
 * <em>Warning:</em> The created observable uses {@link TextView.OnEditorActionListener} to
 * observe actions. Only one observable can be used for a view at a time.
 *
 * @param handled Function invoked each occurrence to determine the return value of the
 * underlying {@link TextView.OnEditorActionListener}.
 */
public inline fun TextView.editorActions(handled: Func1<in Int, Boolean> = Functions.FUNC1_ALWAYS_TRUE): Observable<Int> = RxTextView.editorActions(this, handled)

/**
 * Create an observable of editor action events on {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * <p>
 * <em>Warning:</em> The created observable uses {@link TextView.OnEditorActionListener} to
 * observe actions. Only one observable can be used for a view at a time.
 *
 * @param handled Function invoked each occurrence to determine the return value of the
 * underlying {@link TextView.OnEditorActionListener}.
 */
public inline fun TextView.editorActionEvents(handled: Func1<in TextViewEditorActionEvent, Boolean> = Functions.FUNC1_ALWAYS_TRUE): Observable<TextViewEditorActionEvent> = RxTextView.editorActionEvents(this, handled)

/**
 * Create an observable of character sequences for text changes on {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
public inline fun TextView.textChanges(): Observable<CharSequence> = RxTextView.textChanges(this)

/**
 * Create an observable of text change events for {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
public inline fun TextView.textChangeEvents(): Observable<TextViewTextChangeEvent> = RxTextView.textChangeEvents(this)

/**
 * An action which sets the text property of {@code view} with character sequences.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
public inline fun TextView.text(): Action1<in CharSequence> = RxTextView.text(this)

/**
 * An action which sets the text property of {@code view} string resource IDs.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
public inline fun TextView.textRes(): Action1<in Int> = RxTextView.textRes(this)
