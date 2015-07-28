package com.jakewharton.rxbinding.widget

import android.widget.Adapter
import rx.Observable

/** Create an observable of data change events for {@code adapter}. */
public inline fun <T : Adapter> T.dataChanges(): Observable<T> = RxAdapter.dataChanges(this)

