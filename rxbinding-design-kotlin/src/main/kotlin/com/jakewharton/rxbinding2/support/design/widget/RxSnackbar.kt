@file:Suppress(
    names = "NOTHING_TO_INLINE"
)

package com.jakewharton.rxbinding2.support.design.widget

import android.support.annotation.CheckResult
import android.support.design.widget.Snackbar
import io.reactivex.Observable

/**
 * Create an observable which emits the dismiss events from `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
inline fun Snackbar.dismisses(): Observable<Int> = RxSnackbar.dismisses(this)
