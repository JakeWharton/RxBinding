package com.jakewharton.rxbinding2.support.v7.widget

import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.RecyclerView.ViewHolder
import com.jakewharton.rxbinding2.InitialValueObservable
import io.reactivex.Observable

/**
 * Create an observable of data change events for `RecyclerView.adapter`.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
inline fun <T : Adapter<out ViewHolder>> T.dataChanges(): InitialValueObservable<T> = RxRecyclerViewAdapter.dataChanges(this)
