@file:Suppress(
    names = "NOTHING_TO_INLINE"
)

package com.jakewharton.rxbinding2.support.v4.view

import android.support.annotation.CheckResult
import android.support.v4.view.ViewPager
import com.jakewharton.rxbinding2.InitialValueObservable
import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * Create an observable of scroll state change events on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
inline fun ViewPager.pageScrollStateChanges(): Observable<Int> = RxViewPager.pageScrollStateChanges(this)

/**
 * Create an observable of page selected events on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
inline fun ViewPager.pageSelections(): InitialValueObservable<Int> = RxViewPager.pageSelections(this)

/**
 * An action which sets the current item of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
inline fun ViewPager.currentItem(): Consumer<in Int> = RxViewPager.currentItem(this)
