@file:JvmName("RxSwipeRefreshLayout")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.swiperefreshlayout

import androidx.annotation.CheckResult
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

import com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread

/**
 * Create an observable of refresh events on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
fun SwipeRefreshLayout.refreshes(): Observable<Any> {
  return SwipeRefreshLayoutRefreshObservable(this)
}

private class SwipeRefreshLayoutRefreshObservable(
  private val view: SwipeRefreshLayout
) : Observable<Any>() {

  override fun subscribeActual(observer: Observer<in Any>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.setOnRefreshListener(listener)
  }

  private class Listener(
    private val view: SwipeRefreshLayout,
    private val observer: Observer<in Any>
  ) : MainThreadDisposable(), OnRefreshListener {

    override fun onRefresh() {
      if (!isDisposed) {
        observer.onNext(Unit)
      }
    }

    override fun onDispose() {
      view.setOnRefreshListener(null)
    }
  }
}
