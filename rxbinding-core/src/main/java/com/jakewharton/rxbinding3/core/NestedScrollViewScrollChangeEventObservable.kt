@file:JvmName("RxNestedScrollView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.core

import androidx.annotation.CheckResult
import androidx.core.widget.NestedScrollView
import androidx.core.widget.NestedScrollView.OnScrollChangeListener
import com.jakewharton.rxbinding3.view.ViewScrollChangeEvent
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

import com.jakewharton.rxbinding3.internal.checkMainThread

/**
 * Create an observable of scroll-change events for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`.
 * Unsubscribe to free this reference.
 */
@CheckResult
fun NestedScrollView.scrollChangeEvents(): Observable<ViewScrollChangeEvent> {
  return NestedScrollViewScrollChangeEventObservable(this)
}

private class NestedScrollViewScrollChangeEventObservable(
  private val view: NestedScrollView
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
    private val view: NestedScrollView,
    private val observer: Observer<in ViewScrollChangeEvent>
  ) : MainThreadDisposable(), OnScrollChangeListener {

    override fun onScrollChange(
      v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int,
      oldScrollY: Int
    ) {
      if (!isDisposed) {
        observer.onNext(ViewScrollChangeEvent(view, scrollX, scrollY, oldScrollX, oldScrollY))
      }
    }

    override fun onDispose() {
      view.setOnScrollChangeListener(null as OnScrollChangeListener?)
    }
  }
}
