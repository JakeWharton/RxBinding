@file:Suppress(
    names = "NOTHING_TO_INLINE"
)

package com.jakewharton.rxbinding2.support.v17.leanback.widget

import android.support.annotation.CheckResult
import android.support.v17.leanback.widget.SearchBar
import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * Create an observable of {@linkplain SearchBarSearchQueryEvent search query events} on {@code
 * view}.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
inline fun SearchBar.searchQueryChangeEvents(): Observable<SearchBarSearchQueryEvent> = RxSearchBar.searchQueryChangeEvents(this)

/**
 * Create an observable of String values for search query changes on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
inline fun SearchBar.searchQueryChanges(): Observable<String> = RxSearchBar.searchQueryChanges(this)

/**
 * An action which sets the searchQuery property of `view` with String values.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
inline fun SearchBar.searchQuery(): Consumer<in String> = RxSearchBar.searchQuery(this)
