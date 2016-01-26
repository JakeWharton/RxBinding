package com.jakewharton.rxbinding.support.v17.leanback.widget

import android.support.v17.leanback.widget.SearchBar
import rx.Observable
import rx.functions.Action1

/**
 * Create an observable of {@linkplain SearchBarSearchQueryEvent search query events} on {@code
 * view}.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 */
public inline fun SearchBar.searchQueryChangeEvents(): Observable<SearchBarSearchQueryEvent> = RxSearchBar.searchQueryChangeEvents(this)

/**
 * Create an observable of String values for search query changes on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 */
public inline fun SearchBar.searchQueryChanges(): Observable<String> = RxSearchBar.searchQueryChanges(this)

/**
 * An action which sets the searchQuery property of `view` with String values.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun SearchBar.searchQuery(): Action1<in String> = RxSearchBar.searchQuery(this)
