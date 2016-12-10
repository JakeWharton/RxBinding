package com.jakewharton.rxbinding2.support.v4.view

import android.support.v4.view.ViewPager
import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * Create an observable of scroll state change events on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 */
inline fun ViewPager.pageScrollStateChanges(): Observable<Int> = RxViewPager.pageScrollStateChanges(this)

/**
 * Create an observable of page selected events on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
inline fun ViewPager.pageSelections(): Observable<Int> = RxViewPager.pageSelections(this)

/**
 * An action which sets the current item of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun ViewPager.currentItem(): Consumer<in Int> = RxViewPager.currentItem(this)
