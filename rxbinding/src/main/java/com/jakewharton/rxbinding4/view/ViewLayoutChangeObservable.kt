@file:JvmName("RxView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding4.view

import android.view.View
import android.view.View.OnLayoutChangeListener
import androidx.annotation.CheckResult
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

import com.jakewharton.rxbinding4.internal.checkMainThread

/**
 * Create an observable which emits on `view` layout changes. The emitted value is
 * unspecified and should only be used as notification.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
fun View.layoutChanges(): Observable<Unit> {
  return ViewLayoutChangeObservable(this)
}

private class ViewLayoutChangeObservable(
  private val view: View
) : Observable<Unit>() {

  override fun subscribeActual(observer: Observer<in Unit>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.addOnLayoutChangeListener(listener)
  }

  private class Listener(
    private val view: View,
    private val observer: Observer<in Unit>
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
