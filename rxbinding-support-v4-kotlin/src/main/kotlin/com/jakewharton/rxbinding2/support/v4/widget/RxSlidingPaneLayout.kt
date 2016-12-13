package com.jakewharton.rxbinding2.support.v4.widget

import android.support.v4.widget.SlidingPaneLayout
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
inline fun SlidingPaneLayout.panelOpens(): Observable<Boolean> = RxSlidingPaneLayout.panelOpens(this)

/**
 * Create an observable of the slide offset of the pane of `view`
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [SlidingPaneLayout.setPanelSlideListener]
 * to observe dismiss change. Only one observable can be used for a view at a time.
 */
inline fun SlidingPaneLayout.panelSlides(): Observable<Float> = RxSlidingPaneLayout.panelSlides(this)

/**
 * An action which sets whether the pane of `view` is open.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun SlidingPaneLayout.open(): Consumer<in Boolean> = RxSlidingPaneLayout.open(this)
