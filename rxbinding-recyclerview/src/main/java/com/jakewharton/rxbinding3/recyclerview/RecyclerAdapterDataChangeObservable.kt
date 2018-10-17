@file:JvmMultifileClass
@file:JvmName("RxRecyclerViewAdapter")

package com.jakewharton.rxbinding3.recyclerview

import androidx.annotation.CheckResult
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.jakewharton.rxbinding3.InitialValueObservable
import com.jakewharton.rxbinding3.internal.checkMainThread
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

/**
 * Create an observable of data change events for `RecyclerView.adapter`.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
fun <T : Adapter<out ViewHolder>> T.dataChanges(): InitialValueObservable<T> =
    RecyclerAdapterDataChangeObservable(this)

private class RecyclerAdapterDataChangeObservable<T : Adapter<out ViewHolder>>(
  private val adapter: T
) : InitialValueObservable<T>() {

  override fun subscribeListener(observer: Observer<in T>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(
        adapter, observer)
    observer.onSubscribe(listener)
    adapter.registerAdapterDataObserver(listener.dataObserver)
  }

  override val initialValue get() = adapter

  class Listener<T : Adapter<out ViewHolder>>(
    private val recyclerAdapter: T,
    observer: Observer<in T>
  ) : MainThreadDisposable() {

    val dataObserver = object : AdapterDataObserver() {
      override fun onChanged() {
        if (!isDisposed) {
          observer.onNext(recyclerAdapter)
        }
      }
    }

    override fun onDispose() {
      recyclerAdapter.unregisterAdapterDataObserver(dataObserver)
    }
  }
}
