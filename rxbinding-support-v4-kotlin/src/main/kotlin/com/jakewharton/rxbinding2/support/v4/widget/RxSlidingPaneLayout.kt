@file:Suppress(
    names = "NOTHING_TO_INLINE"
)

package com.jakewharton.rxbinding2.support.v4.widget

import android.support.annotation.CheckResult
import android.support.v4.widget.SlidingPaneLayout
import com.jakewharton.rxbinding2.InitialValueObservable
import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * Create an observable of the open state of the pane of `view`
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [SlidingPaneLayout.setPanelSlideListener]
 * to observe dismiss change. Only one observable can be used for a view at a time.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
inline fun SlidingPaneLayout.panelOpens(): InitialValueObservable<Boolean> = RxSlidingPaneLayout.panelOpens(this)

/**
 * Create an observable of the slide offset of the pane of `view`
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [SlidingPaneLayout.setPanelSlideListener]
 * to observe dismiss change. Only one observable can be used for a view at a time.
 */
@CheckResult
inline fun SlidingPaneLayout.panelSlides(): Observable<Float> = RxSlidingPaneLayout.panelSlides(this)

/**
 * An action which sets whether the pane of `view` is open.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
inline fun SlidingPaneLayout.open(): Consumer<in Boolean> = RxSlidingPaneLayout.open(this)
