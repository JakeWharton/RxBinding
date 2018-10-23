@file:JvmName("RxAdapterView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.widget

import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.annotation.CheckResult
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

import com.jakewharton.rxbinding3.internal.checkMainThread

/**
 * Create an observable of the position of item clicks for `view`.
 *
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
fun <T : Adapter> AdapterView<T>.itemClicks(): Observable<Int> {
  return AdapterViewItemClickObservable(this)
}

private class AdapterViewItemClickObservable(
  private val view: AdapterView<*>
) : Observable<Int>() {

  override fun subscribeActual(observer: Observer<in Int>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.onItemClickListener = listener
  }

  private class Listener(
    private val view: AdapterView<*>,
    private val observer: Observer<in Int>
  ) : MainThreadDisposable(), OnItemClickListener {

    override fun onItemClick(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
      if (!isDisposed) {
        observer.onNext(position)
      }
    }

    override fun onDispose() {
      view.onItemClickListener = null
    }
  }
}
