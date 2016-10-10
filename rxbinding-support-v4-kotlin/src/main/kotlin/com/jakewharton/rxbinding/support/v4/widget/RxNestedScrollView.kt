package com.jakewharton.rxbinding.support.v4.widget

import android.support.v4.widget.NestedScrollView
import com.jakewharton.rxbinding.view.ViewScrollChangeEvent
import rx.Observable

/**
 * Create an observable of scroll-change events for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`.
 * Unsubscribe to free this reference.
 */
inline fun NestedScrollView.scrollChangeEvents(): Observable<ViewScrollChangeEvent> = RxNestedScrollView.scrollChangeEvents(this)
