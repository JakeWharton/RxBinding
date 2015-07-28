package com.jakewharton.rxbinding.widget

import android.widget.TextView
import rx.Observable
import com.jakewharton.rxbinding.internal.Functions
import rx.functions.Action1
import rx.functions.Func1

/**
 * Create an observable of editor actions on {@code view}.
 * 
 * *Warning:* The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * 
 * *Warning:* The created observable uses {@link TextView.OnEditorActionListener} to
 * observe actions. Only one observable can be used for a view at a time.
 */
public inline fun TextView.editorActions(): Observable<Int> = RxTextView.editorActions(this)

/**
 * Create an observable of editor actions on {@code view}.
 * 
 * *Warning:* The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * 
 * *Warning:* The created observable uses {@link TextView.OnEditorActionListener} to
 * observe actions. Only one observable can be used for a view at a time.
 *
 * @param handled Function invoked each occurrence to determine the return value of the
 * underlying {@link TextView.OnEditorActionListener}.
 */
public inline fun TextView.editorActions(handled: Func1<in Int, Boolean>): Observable<Int> = RxTextView.editorActions(this, handled)

/**
 * Create an observable of editor action events on {@code view}.
 * 
 * *Warning:* The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * 
 * *Warning:* The created observable uses {@link TextView.OnEditorActionListener} to
 * observe actions. Only one observable can be used for a view at a time.
 */
public inline fun TextView.editorActionEvents(): Observable<TextViewEditorActionEvent> = RxTextView.editorActionEvents(this)

/**
 * Create an observable of editor action events on {@code view}.
 * 
 * *Warning:* The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 * 
 * *Warning:* The created observable uses {@link TextView.OnEditorActionListener} to
 * observe actions. Only one observable can be used for a view at a time.
 *
 * @param handled Function invoked each occurrence to determine the return value of the
 * underlying {@link TextView.OnEditorActionListener}.
 */
public inline fun TextView.editorActionEvents(handled: Func1<in TextViewEditorActionEvent, Boolean>): Observable<TextViewEditorActionEvent> = RxTextView.editorActionEvents(this, handled)

/**
 * Create an observable of character sequences for text changes on {@code view}.
 * 
 * *Warning:* The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
public inline fun TextView.textChanges(): Observable<CharSequence> = RxTextView.textChanges(this)

/**
 * Create an observable of text change events for {@code view}.
 * 
 * *Warning:* The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
public inline fun TextView.textChangeEvents(): Observable<TextViewTextChangeEvent> = RxTextView.textChangeEvents(this)

/**
 * An action which sets the text property of {@code view} with character sequences.
 * 
 * *Warning:* The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
public inline fun TextView.text(): Action1<in CharSequence> = RxTextView.text(this)

/**
 * An action which sets the text property of {@code view} string resource IDs.
 * 
 * *Warning:* The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
public inline fun TextView.textRes(): Action1<in Int> = RxTextView.textRes(this)

