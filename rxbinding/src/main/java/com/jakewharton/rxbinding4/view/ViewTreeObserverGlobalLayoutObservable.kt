@file:JvmName("RxView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding4.view

import android.view.View
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.annotation.CheckResult
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

import com.jakewharton.rxbinding4.internal.checkMainThread

/**
 * Create an observable which emits on `view` globalLayout events. The emitted value is
 * unspecified and should only be used as notification.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [ ][ViewTreeObserver.addOnGlobalLayoutListener] to observe global layouts. Multiple observables
 * can be used for a view at a time.
 */
@CheckResult
fun View.globalLayouts(): Observable<Unit> {
  return ViewTreeObserverGlobalLayoutObservable(this)
}

private class ViewTreeObserverGlobalLayoutObservable(
  private val view: View
) : Observable<Unit>() {

  override fun subscribeActual(observer: Observer<in Unit>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.viewTreeObserver
        .addOnGlobalLayoutListener(listener)
  }

  private class Listener(
    private val view: View,
    private val observer: Observer<in Unit>
  ) : MainThreadDisposable(), OnGlobalLayoutListener {

    override fun onGlobalLayout() {
      if (!isDisposed) {
        observer.onNext(Unit)
      }
    }

    override fun onDispose() {
      @Suppress("DEPRECATION") // Correct when minSdk 16.
      view.viewTreeObserver.removeGlobalOnLayoutListener(this)
    }
  }
}
