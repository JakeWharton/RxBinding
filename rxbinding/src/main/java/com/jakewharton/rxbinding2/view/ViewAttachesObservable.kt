@file:JvmName("RxView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding2.view

import android.view.View
import android.view.View.OnAttachStateChangeListener
import androidx.annotation.CheckResult
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

import com.jakewharton.rxbinding2.internal.checkMainThread

/**
 * Create an observable which emits on `view` attach events. The emitted value is
 * unspecified and should only be used as notification.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
fun View.attaches(): Observable<Any> {
  return ViewAttachesObservable(this, true)
}

/**
 * Create an observable which emits on `view` detach events. The emitted value is
 * unspecified and should only be used as notification.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
fun View.detaches(): Observable<Any> {
  return ViewAttachesObservable(this, false)
}

private class ViewAttachesObservable(
  private val view: View,
  private val callOnAttach: Boolean
) : Observable<Any>() {

  override fun subscribeActual(observer: Observer<in Any>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, callOnAttach, observer)
    observer.onSubscribe(listener)
    view.addOnAttachStateChangeListener(listener)
  }

  private class Listener(
    private val view: View,
    private val callOnAttach: Boolean,
    private val observer: Observer<in Any>
  ) : MainThreadDisposable(), OnAttachStateChangeListener {

    override fun onViewAttachedToWindow(v: View) {
      if (callOnAttach && !isDisposed) {
        observer.onNext(Unit)
      }
    }

    override fun onViewDetachedFromWindow(v: View) {
      if (!callOnAttach && !isDisposed) {
        observer.onNext(Unit)
      }
    }

    override fun onDispose() {
      view.removeOnAttachStateChangeListener(this)
    }
  }
}
