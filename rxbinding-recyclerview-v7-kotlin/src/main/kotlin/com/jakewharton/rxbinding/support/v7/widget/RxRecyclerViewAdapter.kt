package com.jakewharton.rxbinding.support.v7.widget

import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.RecyclerView.ViewHolder
import rx.Observable

/**
 * Create an observable of data change events for `RecyclerView.adapter`.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
public inline fun <T : Adapter<out ViewHolder>> T.dataChanges(): Observable<T> = RxRecyclerViewAdapter.dataChanges(this)
