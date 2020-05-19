@file:JvmMultifileClass
@file:JvmName("RxRecyclerView")

package com.jakewharton.rxbinding4.recyclerview

import android.content.Context
import androidx.annotation.CheckResult
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

import com.jakewharton.rxbinding4.internal.checkMainThread

/**
 * Create an observable of scroll events on `recyclerView`.
 *
 * *Warning:* The created observable keeps a strong reference to `recyclerView`.
 * Unsubscribe to free this reference.
 */
@CheckResult
fun RecyclerView.scrollEvents(): Observable<RecyclerViewScrollEvent> =
    RecyclerViewScrollEventObservable(this)

/**
 * A scroll event on a recyclerView.
 *
 * **Warning:** Instances keep a strong reference to the recyclerView. Operators that
 * cache instances have the potential to leak the associated [Context].
 */
data class RecyclerViewScrollEvent(val view: RecyclerView, val dx: Int, val dy: Int)

private class RecyclerViewScrollEventObservable(
  private val view: RecyclerView
) : Observable<RecyclerViewScrollEvent>() {

  override fun subscribeActual(observer: Observer<in RecyclerViewScrollEvent>) {
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
    observer: Observer<in RecyclerViewScrollEvent>
  ) : MainThreadDisposable() {

    val scrollListener = object : RecyclerView.OnScrollListener() {
      override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (!isDisposed) {
          observer.onNext(
              RecyclerViewScrollEvent(recyclerView, dx, dy))
        }
      }
    }

    override fun onDispose() {
      recyclerView.removeOnScrollListener(scrollListener)
    }
  }
}
