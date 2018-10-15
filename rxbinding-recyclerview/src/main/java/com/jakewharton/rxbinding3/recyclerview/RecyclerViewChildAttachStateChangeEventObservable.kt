@file:JvmMultifileClass
@file:JvmName("RxRecyclerView")

package com.jakewharton.rxbinding3.recyclerview

import android.view.View
import androidx.annotation.CheckResult
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

import com.jakewharton.rxbinding2.internal.checkMainThread

/**
 * Create an observable of child attach state change events on `recyclerView`.
 *
 * *Warning:* The created observable keeps a strong reference to `recyclerView`.
 * Unsubscribe to free this reference.
 */
@CheckResult
fun RecyclerView.childAttachStateChangeEvents(): Observable<RecyclerViewChildAttachStateChangeEvent> =
    RecyclerViewChildAttachStateChangeEventObservable(this)

private class RecyclerViewChildAttachStateChangeEventObservable(
  private val view: RecyclerView
) : Observable<RecyclerViewChildAttachStateChangeEvent>() {

  override fun subscribeActual(observer: Observer<in RecyclerViewChildAttachStateChangeEvent>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(
        view, observer)
    observer.onSubscribe(listener)
    view.addOnChildAttachStateChangeListener(listener)
  }

  class Listener(
    private val recyclerView: RecyclerView,
    private val observer: Observer<in RecyclerViewChildAttachStateChangeEvent>
  ) : MainThreadDisposable(), OnChildAttachStateChangeListener {

    override fun onChildViewAttachedToWindow(childView: View) {
      if (!isDisposed) {
        observer.onNext(RecyclerViewChildAttachEvent(recyclerView, childView))
      }
    }

    override fun onChildViewDetachedFromWindow(childView: View) {
      if (!isDisposed) {
        observer.onNext(RecyclerViewChildDetachEvent(recyclerView, childView))
      }
    }

    override fun onDispose() {
      recyclerView.removeOnChildAttachStateChangeListener(this)
    }
  }
}
