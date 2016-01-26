package com.jakewharton.rxbinding.support.v4.widget

import android.support.v4.widget.SwipeRefreshLayout
import rx.Observable
import rx.functions.Action1

/**
 * Create an observable of refresh events on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun SwipeRefreshLayout.refreshes(): Observable<Unit> = RxSwipeRefreshLayout.refreshes(this).map { Unit }

/**
 * An action which sets whether the layout is showing the refreshing indicator.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun SwipeRefreshLayout.refreshing(): Action1<in Boolean> = RxSwipeRefreshLayout.refreshing(this)
