package com.jakewharton.rxbinding.support.v17.leanback.widget

import android.support.v17.leanback.widget.SearchBar
import com.jakewharton.rxbinding.support.v17.leanback.widget.SearchBarSearchQueryEvent
import rx.Observable
import rx.functions.Action1

/**
 * Create an observable of {@linkplain SearchBarSearchQueryEvent search query events} on {@code
 * view}.
 * 
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 * 
 * *Note:* A value will not be emitted on subscribe.
 */
public inline fun SearchBar.searchQueryChangeEvents(): Observable<SearchBarSearchQueryEvent> = RxSearchBar.searchQueryChangeEvents(this)

/**
 * Create an observable of character sequences for query text changes on `view`.
 * 
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 * 
 * *Note:* A value will not be emitted on subscribe.
 */
public inline fun SearchBar.searchQueryChanges(): Observable<CharSequence> = RxSearchBar.searchQueryChanges(this)

/**
 * An action which sets the searchQuery property of `view` with character sequences.
 * 
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun SearchBar.searchQuery(): Action1<in CharSequence> = RxSearchBar.searchQuery(this)
