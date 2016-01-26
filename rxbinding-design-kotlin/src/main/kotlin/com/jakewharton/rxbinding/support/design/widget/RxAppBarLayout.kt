package com.jakewharton.rxbinding.support.design.widget

import android.support.design.widget.AppBarLayout
import rx.Observable

/**
 * Create an observable which emits the offset change in `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun AppBarLayout.offsetChanges(): Observable<Int> = RxAppBarLayout.offsetChanges(this)
