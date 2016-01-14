package com.jakewharton.rxbinding.widget

import android.widget.SearchView
import com.jakewharton.rxbinding.internal.Functions
import rx.Observable
import rx.functions.Action1
import rx.functions.Func1

/**
 * Create an observable of {@linkplain SearchViewQueryTextEvent query text events} on {@code
 * view}.
 * 
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 * 
 * *Note:* A value will be emitted immediately on subscribe.
 */
public inline fun SearchView.queryTextChangeEvents(): Observable<SearchViewQueryTextEvent> = RxSearchView.queryTextChangeEvents(this)

/**
 * Create an observable of character sequences for query text changes on `view`.
 * 
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 * 
 * *Note:* A value will be emitted immediately on subscribe.
 */
public inline fun SearchView.queryTextChanges(): Observable<CharSequence> = RxSearchView.queryTextChanges(this)

/**
 * An action which sets the query property of `view` with character sequences.
 * 
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * @param submit weather to submit query right after updating query text
 */
public inline fun SearchView.query(submit: Boolean): Action1<in CharSequence> = RxSearchView.query(this, submit)

/**
 * Create an observable of booleans representing the focus of the query text field.
 * 
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 * 
 */
public inline fun SearchView.queryTextFocusChange(): Observable<Boolean> = RxSearchView.queryTextFocusChange(this)

/**
 * Create an observable of the absolute position of the clicked item in the list of suggestions
 * 
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 * 
 * *Warning:* The created observable uses [SearchView.setOnSuggestionListener] to
 * observe search view events. Only one observable can be used for a search view at a time.
 */
public inline fun SearchView.suggestionClick(): Observable<Int> = RxSearchView.suggestionClick(this)

/**
 * Create an observable of the absolute position of the clicked item in the list of suggestions
 * 
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 * 
 * *Warning:* The created observable uses [SearchView.setOnSuggestionListener] to
 * observe search view events. Only one observable can be used for a search view at a time.
 *
 * @param handled Function invoked with each value to determine the return value of the
 * underlying [SearchView.OnSuggestionListener].
 */
public inline fun SearchView.suggestionClick(handled: Func1<in Int, Boolean>): Observable<Int> = RxSearchView.suggestionClick(this, handled)
