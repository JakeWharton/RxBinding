package com.jakewharton.rxbinding.support.v4.view

import android.support.v4.view.ViewPager
import rx.Observable

/**
 * Create an observable of page selected events on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
public inline fun ViewPager.pageSelections(): Observable<Int> = RxViewPager.pageSelections(this)
