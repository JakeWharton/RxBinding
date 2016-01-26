package com.jakewharton.rxbinding.support.design.widget

import android.support.design.widget.TabLayout
import android.support.design.widget.TabLayout.Tab
import rx.Observable
import rx.functions.Action1

/**
 * Create an observable which emits the selected tab in `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* If a tab is already selected, it will be emitted immediately on subscribe.
 */
public inline fun TabLayout.selections(): Observable<Tab> = RxTabLayout.selections(this)

/**
 * Create an observable which emits selection, reselection, and unselection events for the tabs
 * in `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* If a tab is already selected, an event will be emitted immediately on subscribe.
 */
public inline fun TabLayout.selectionEvents(): Observable<TabLayoutSelectionEvent> = RxTabLayout.selectionEvents(this)

/**
 * An action which sets the selected tab of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun TabLayout.select(): Action1<in Int> = RxTabLayout.select(this)
