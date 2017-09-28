@file:Suppress(
    names = "NOTHING_TO_INLINE"
)

package com.jakewharton.rxbinding2.support.v4.widget

import android.support.annotation.CheckResult
import android.support.v4.widget.NestedScrollView
import com.jakewharton.rxbinding2.view.ViewScrollChangeEvent
import io.reactivex.Observable

/**
 * Create an observable of scroll-change events for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`.
 * Unsubscribe to free this reference.
 */
@CheckResult
inline fun NestedScrollView.scrollChangeEvents(): Observable<ViewScrollChangeEvent> = RxNestedScrollView.scrollChangeEvents(this)
