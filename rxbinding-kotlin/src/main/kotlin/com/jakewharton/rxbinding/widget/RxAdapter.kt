package com.jakewharton.rxbinding.widget

import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.BaseAdapter
import rx.Observable

/** Create an observable of data change events for {@code adapter}. */
public inline fun <T : Adapter> T.dataChanges(): Observable<T> = RxAdapter.dataChanges(this)
