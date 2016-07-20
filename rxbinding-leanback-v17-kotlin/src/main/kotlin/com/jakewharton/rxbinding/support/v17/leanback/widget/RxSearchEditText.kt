package com.jakewharton.rxbinding.support.v17.leanback.widget

import android.support.v17.leanback.widget.SearchEditText
import rx.Observable

/**
 * Create an observable which emits the keyboard dismiss events from `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun SearchEditText.keyboardDismisses(): Observable<Unit> = RxSearchEditText.keyboardDismisses(this).map { Unit }
