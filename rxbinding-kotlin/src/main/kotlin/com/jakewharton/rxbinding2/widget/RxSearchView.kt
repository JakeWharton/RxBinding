package com.jakewharton.rxbinding2.widget

import android.widget.SearchView
import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * Create an observable of {@linkplain SearchViewQueryTextEvent query text events} on {@code
 * view}.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
inline fun SearchView.queryTextChangeEvents(): Observable<SearchViewQueryTextEvent> = RxSearchView.queryTextChangeEvents(this)

/**
 * Create an observable of character sequences for query text changes on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
inline fun SearchView.queryTextChanges(): Observable<CharSequence> = RxSearchView.queryTextChanges(this)

/**
 * An action which sets the query property of `view` with character sequences.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * @param submit weather to submit query right after updating query text
 */
inline fun SearchView.query(submit: Boolean): Consumer<in CharSequence> = RxSearchView.query(this, submit)
