@file:Suppress(
    names = "NOTHING_TO_INLINE"
)

package com.jakewharton.rxbinding2.support.design.widget

import android.support.annotation.CheckResult
import android.support.design.widget.AppBarLayout
import io.reactivex.Observable

/**
 * Create an observable which emits the offset change in `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
inline fun AppBarLayout.offsetChanges(): Observable<Int> = RxAppBarLayout.offsetChanges(this)
