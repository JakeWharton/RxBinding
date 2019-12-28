@file:JvmMultifileClass
@file:JvmName("RxRecyclerView")

package com.jakewharton.rxbinding4.recyclerview

import androidx.annotation.CheckResult
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

import com.jakewharton.rxbinding4.internal.checkMainThread

/**
 * Create an observable of scroll state changes on `recyclerView`.
 *
 * *Warning:* The created observable keeps a strong reference to `recyclerView`.
 * Unsubscribe to free this reference.
 */
@CheckResult
fun RecyclerView.scrollStateChanges(): Observable<Int> =
    RecyclerViewScrollStateChangeObservable(this)

private class RecyclerViewScrollStateChangeObservable(
  private val view: RecyclerView
) : Observable<Int>() {

  override fun subscribeActual(observer: Observer<in Int>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(
        view, observer)
    observer.onSubscribe(listener)
    view.addOnScrollListener(listener.scrollListener)
  }

  class Listener(
    private val recyclerView: RecyclerView,
    observer: Observer<in Int>
  ) : MainThreadDisposable() {

    val scrollListener = object : RecyclerView.OnScrollListener() {
      override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (!isDisposed) {
          observer.onNext(newState)
        }
      }
    }

    override fun onDispose() {
      recyclerView.removeOnScrollListener(scrollListener)
    }
  }
}
