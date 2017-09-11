@file:Suppress(
    names = "NOTHING_TO_INLINE"
)

package com.jakewharton.rxbinding2.support.design.widget

import android.support.design.widget.Snackbar
import android.view.View
import io.reactivex.Observable
import kotlin.Int
import kotlin.Suppress

/**
 * Create an observable which emits the dismiss events from `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun Snackbar.dismisses(): Observable<Int> = RxSnackbar.dismisses(this)

/**
 * Create an observable which emits the action clicked events from `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun Snackbar.actionClicked(resId: Int): Observable<View> = RxSnackbar.actionClicked(this, resId)

/**
 * Create an observable which emits the action clicked events from `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun Snackbar.actionClicked(text: CharSequence): Observable<View> = RxSnackbar.actionClicked(this, text)
