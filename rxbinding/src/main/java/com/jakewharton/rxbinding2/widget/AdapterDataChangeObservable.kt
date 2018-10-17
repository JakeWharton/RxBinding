@file:JvmName("RxAdapter")
@file:JvmMultifileClass

package com.jakewharton.rxbinding2.widget

import android.database.DataSetObserver
import android.widget.Adapter
import androidx.annotation.CheckResult
import com.jakewharton.rxbinding2.InitialValueObservable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

import com.jakewharton.rxbinding2.internal.checkMainThread

/**
 * Create an observable of data change events for `adapter`.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
fun <T : Adapter> T.dataChanges(): InitialValueObservable<T> {
  return AdapterDataChangeObservable(this)
}

private class AdapterDataChangeObservable<T : Adapter>(
  private val adapter: T
) : InitialValueObservable<T>() {

  override fun subscribeListener(observer: Observer<in T>) {
    if (!checkMainThread(observer)) {
      return
    }
    val disposableDataSetObserver = ObserverDisposable(initialValue, observer)
    initialValue.registerDataSetObserver(disposableDataSetObserver.dataSetObserver)
    observer.onSubscribe(disposableDataSetObserver)
  }

  override val initialValue get() = adapter

  private class ObserverDisposable<T : Adapter>(
    private val adapter: T,
    observer: Observer<in T>
  ) : MainThreadDisposable() {
    @JvmField val dataSetObserver = object : DataSetObserver() {
      override fun onChanged() {
        if (!isDisposed) {
          observer.onNext(adapter)
        }
      }
    }

    override fun onDispose() {
      adapter.unregisterDataSetObserver(dataSetObserver)
    }
  }
}
