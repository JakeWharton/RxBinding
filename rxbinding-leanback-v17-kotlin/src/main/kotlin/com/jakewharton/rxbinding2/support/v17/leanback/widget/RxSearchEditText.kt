package com.jakewharton.rxbinding2.support.v17.leanback.widget

import android.support.v17.leanback.widget.SearchEditText
import io.reactivex.Observable

/**
 * Create an observable which emits the keyboard dismiss events from `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun SearchEditText.keyboardDismisses(): Observable<Any> = RxSearchEditText.keyboardDismisses(this)
