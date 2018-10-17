@file:JvmName("RxView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.view

import android.view.View
import android.view.View.OnLayoutChangeListener
import androidx.annotation.CheckResult
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

import com.jakewharton.rxbinding3.internal.checkMainThread

/**
 * Create an observable which emits on `view` layout changes. The emitted value is
 * unspecified and should only be used as notification.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
fun View.layoutChanges(): Observable<Any> {
  return ViewLayoutChangeObservable(this)
}

private class ViewLayoutChangeObservable(
  private val view: View
) : Observable<Any>() {

  override fun subscribeActual(observer: Observer<in Any>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.addOnLayoutChangeListener(listener)
  }

  private class Listener(
    private val view: View,
    private val observer: Observer<in Any>
  ) : MainThreadDisposable(), OnLayoutChangeListener {

    override fun onLayoutChange(
      v: View, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int,
      oldTop: Int, oldRight: Int, oldBottom: Int
    ) {
      if (!isDisposed) {
        observer.onNext(Unit)
      }
    }

    override fun onDispose() {
      view.removeOnLayoutChangeListener(this)
    }
  }
}
