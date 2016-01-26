package com.jakewharton.rxbinding.support.v7.widget

import android.support.v7.widget.SearchView
import rx.Observable
import rx.functions.Action1

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
