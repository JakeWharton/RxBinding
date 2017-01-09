package com.jakewharton.rxbinding.support.design.widget

import android.support.design.widget.Snackbar
import rx.Observable

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
inline fun Snackbar.actionClicked(resId: Int): Observable<Int> = RxSnackbar.actionClicked(this, resId)

/**
 * Create an observable which emits the action clicked events from `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun Snackbar.actionClicked(text: CharSequence): Observable<Int> = RxSnackbar.actionClicked(this, text)
