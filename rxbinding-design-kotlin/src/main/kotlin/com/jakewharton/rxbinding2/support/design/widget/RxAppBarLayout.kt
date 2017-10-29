@file:Suppress(
    names = "NOTHING_TO_INLINE"
)

package com.jakewharton.rxbinding2.support.design.widget

import android.support.design.widget.AppBarLayout
import io.reactivex.Observable
import kotlin.Int
import kotlin.Suppress

/**
 * Create an observable which emits the offset change in `view`.
 * 
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun AppBarLayout.offsetChanges(): Observable<Int> = RxAppBarLayout.offsetChanges(this)
