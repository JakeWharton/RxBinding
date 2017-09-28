@file:Suppress(
    names = "NOTHING_TO_INLINE"
)

package com.jakewharton.rxbinding2.support.v7.widget

import android.support.annotation.CheckResult
import android.support.v7.widget.RecyclerView
import com.jakewharton.rxbinding2.InitialValueObservable

/**
 * Create an observable of data change events for `RecyclerView.adapter`.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
inline fun <T : RecyclerView.Adapter<out RecyclerView.ViewHolder>> T.dataChanges(): InitialValueObservable<T> = RxRecyclerViewAdapter.dataChanges(this)
