@file:JvmName("RxView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.view

import android.view.KeyEvent
import android.view.View
import android.view.View.OnKeyListener
import androidx.annotation.CheckResult
import com.jakewharton.rxbinding3.internal.AlwaysTrue
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

import com.jakewharton.rxbinding3.internal.checkMainThread

/**
 * Create an observable of key events for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 * *Warning:* The created observable uses [View.setOnKeyListener] to observe
 * key events. Only one observable can be used for a view at a time.
 *
 * @param handled Predicate invoked each occurrence to determine the return value of the
 * underlying [View.OnKeyListener].
 */
@CheckResult
@JvmOverloads
fun View.keys(handled: (KeyEvent) -> Boolean = AlwaysTrue): Observable<KeyEvent> {
  return ViewKeyObservable(this, handled)
}

private class ViewKeyObservable(
  private val view: View,
  private val handled: (KeyEvent) -> Boolean
) : Observable<KeyEvent>() {

  override fun subscribeActual(observer: Observer<in KeyEvent>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, handled, observer)
    observer.onSubscribe(listener)
    view.setOnKeyListener(listener)
  }

  private class Listener(
    private val view: View,
    private val handled: (KeyEvent) -> Boolean,
    private val observer: Observer<in KeyEvent>
  ) : MainThreadDisposable(), OnKeyListener {

    override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
      if (!isDisposed) {
        try {
          if (handled(event)) {
            observer.onNext(event)
            return true
          }
        } catch (e: Exception) {
          observer.onError(e)
          dispose()
        }

      }
      return false
    }

    override fun onDispose() {
      view.setOnKeyListener(null)
    }
  }
}
