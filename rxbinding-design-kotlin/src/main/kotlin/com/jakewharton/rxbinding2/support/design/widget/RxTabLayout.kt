@file:Suppress(
    names = "NOTHING_TO_INLINE"
)

package com.jakewharton.rxbinding2.support.design.widget

import android.support.annotation.CheckResult
import android.support.design.widget.TabLayout
import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * Create an observable which emits the selected tab in `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* If a tab is already selected, it will be emitted immediately on subscribe.
 */
@CheckResult
inline fun TabLayout.selections(): Observable<TabLayout.Tab> = RxTabLayout.selections(this)

/**
 * Create an observable which emits selection, reselection, and unselection events for the tabs
 * in `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* If a tab is already selected, an event will be emitted immediately on subscribe.
 */
@CheckResult
inline fun TabLayout.selectionEvents(): Observable<TabLayoutSelectionEvent> = RxTabLayout.selectionEvents(this)

/**
 * An action which sets the selected tab of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
inline fun TabLayout.select(): Consumer<in Int> = RxTabLayout.select(this)
