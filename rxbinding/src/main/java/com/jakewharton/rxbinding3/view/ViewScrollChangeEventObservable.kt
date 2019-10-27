@file:JvmName("RxView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.view

import androidx.annotation.RequiresApi
import android.view.View
import android.view.View.OnScrollChangeListener
import androidx.annotation.CheckResult
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

import com.jakewharton.rxbinding3.internal.checkMainThread

/**
 * Create an observable of scroll-change events for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@RequiresApi(23)
@CheckResult
fun View.scrollChangeEvents(): Observable<ViewScrollChangeEvent> {
  return ViewScrollChangeEventObservable(this)
}

@RequiresApi(23)
private class ViewScrollChangeEventObservable(
  private val view: View
) : Observable<ViewScrollChangeEvent>() {

  override fun subscribeActual(observer: Observer<in ViewScrollChangeEvent>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.setOnScrollChangeListener(listener)
  }

  private class Listener(
    private val view: View,
    private val observer: Observer<in ViewScrollChangeEvent>
  ) : MainThreadDisposable(), OnScrollChangeListener {

    override fun onScrollChange(
      v: View,
      scrollX: Int,
      scrollY: Int,
      oldScrollX: Int,
      oldScrollY: Int
    ) {
      if (!isDisposed) {
        observer.onNext(ViewScrollChangeEvent(v, scrollX, scrollY, oldScrollX, oldScrollY))
      }
    }

    override fun onDispose() {
      view.setOnScrollChangeListener(null)
    }
  }
}
