@file:JvmName("RxView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding4.view

import android.view.View
import android.view.View.OnClickListener
import androidx.annotation.CheckResult
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

import com.jakewharton.rxbinding4.internal.checkMainThread

/**
 * Create an observable which emits on `view` click events. The emitted value is
 * unspecified and should only be used as notification.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [View.setOnClickListener] to observe
 * clicks. Only one observable can be used for a view at a time.
 */
@CheckResult
fun View.clicks(): Observable<Unit> {
  return ViewClickObservable(this)
}

private class ViewClickObservable(
  private val view: View
) : Observable<Unit>() {

  override fun subscribeActual(observer: Observer<in Unit>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.setOnClickListener(listener)
  }

  private class Listener(
    private val view: View,
    private val observer: Observer<in Unit>
  ) : MainThreadDisposable(), OnClickListener {

    override fun onClick(v: View) {
      if (!isDisposed) {
        observer.onNext(Unit)
      }
    }

    override fun onDispose() {
      view.setOnClickListener(null)
    }
  }
}
