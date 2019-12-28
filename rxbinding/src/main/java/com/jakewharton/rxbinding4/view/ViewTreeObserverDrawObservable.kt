@file:JvmName("RxView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding4.view

import androidx.annotation.RequiresApi
import android.view.View
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnDrawListener
import androidx.annotation.CheckResult
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

import com.jakewharton.rxbinding4.internal.checkMainThread

/**
 * Create an observable for draws on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [ViewTreeObserver.addOnDrawListener] to
 * observe draws. Multiple observables can be used for a view at a time.
 */
@RequiresApi(16)
@CheckResult
fun View.draws(): Observable<Unit> {
  return ViewTreeObserverDrawObservable(this)
}

@RequiresApi(16)
private class ViewTreeObserverDrawObservable(
  private val view: View
) : Observable<Unit>() {

  override fun subscribeActual(observer: Observer<in Unit>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.viewTreeObserver
        .addOnDrawListener(listener)
  }

  private class Listener(
    private val view: View,
    private val observer: Observer<in Unit>
  ) : MainThreadDisposable(), OnDrawListener {

    override fun onDraw() {
      if (!isDisposed) {
        observer.onNext(Unit)
      }
    }

    override fun onDispose() {
      view.viewTreeObserver.removeOnDrawListener(this)
    }
  }
}
