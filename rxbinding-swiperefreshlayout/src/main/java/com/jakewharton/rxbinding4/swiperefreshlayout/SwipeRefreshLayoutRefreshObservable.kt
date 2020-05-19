@file:JvmName("RxSwipeRefreshLayout")
@file:JvmMultifileClass

package com.jakewharton.rxbinding4.swiperefreshlayout

import androidx.annotation.CheckResult
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

import com.jakewharton.rxbinding4.internal.checkMainThread

/**
 * Create an observable of refresh events on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
fun SwipeRefreshLayout.refreshes(): Observable<Unit> {
  return SwipeRefreshLayoutRefreshObservable(this)
}

private class SwipeRefreshLayoutRefreshObservable(
  private val view: SwipeRefreshLayout
) : Observable<Unit>() {

  override fun subscribeActual(observer: Observer<in Unit>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.setOnRefreshListener(listener)
  }

  private class Listener(
    private val view: SwipeRefreshLayout,
    private val observer: Observer<in Unit>
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
