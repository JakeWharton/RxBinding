@file:JvmName("RxView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.view

import android.view.View
import android.view.View.OnAttachStateChangeListener
import androidx.annotation.CheckResult
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

import com.jakewharton.rxbinding3.internal.checkMainThread

/**
 * Create an observable of attach and detach events on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
fun View.attachEvents(): Observable<ViewAttachEvent> {
  return ViewAttachEventObservable(this)
}

private class ViewAttachEventObservable(
  private val view: View
) : Observable<ViewAttachEvent>() {

  override fun subscribeActual(observer: Observer<in ViewAttachEvent>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.addOnAttachStateChangeListener(listener)
  }

  private class Listener(
    private val view: View,
    private val observer: Observer<in ViewAttachEvent>
  ) : MainThreadDisposable(), OnAttachStateChangeListener {

    override fun onViewAttachedToWindow(v: View) {
      if (!isDisposed) {
        observer.onNext(ViewAttachAttachedEvent(view))
      }
    }

    override fun onViewDetachedFromWindow(v: View) {
      if (!isDisposed) {
        observer.onNext(ViewAttachDetachedEvent(view))
      }
    }

    override fun onDispose() {
      view.removeOnAttachStateChangeListener(this)
    }
  }
}
