package com.jakewharton.rxbinding.support.design.widget

import android.support.design.widget.Snackbar
import rx.Observable

/**
 * Create an observable which emits the dismiss events from `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun Snackbar.dismisses(): Observable<Int> = RxSnackbar.dismisses(this)
