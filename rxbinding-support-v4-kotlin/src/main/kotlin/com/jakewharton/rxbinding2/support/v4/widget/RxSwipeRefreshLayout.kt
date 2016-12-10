package com.jakewharton.rxbinding2.support.v4.widget

import android.support.v4.widget.SwipeRefreshLayout
import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * Create an observable of refresh events on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun SwipeRefreshLayout.refreshes(): Observable<Any> = RxSwipeRefreshLayout.refreshes(this)

/**
 * An action which sets whether the layout is showing the refreshing indicator.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun SwipeRefreshLayout.refreshing(): Consumer<in Boolean> = RxSwipeRefreshLayout.refreshing(this)
