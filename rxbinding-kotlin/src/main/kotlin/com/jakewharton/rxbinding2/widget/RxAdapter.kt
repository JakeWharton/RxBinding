package com.jakewharton.rxbinding2.widget

import android.widget.Adapter
import io.reactivex.Observable

/**
 * Create an observable of data change events for `adapter`.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
inline fun <T : Adapter> T.dataChanges(): Observable<T> = RxAdapter.dataChanges(this)
