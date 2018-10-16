@file:JvmName("RxView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding2.view

import android.view.View
import android.view.View.OnLongClickListener
import androidx.annotation.CheckResult
import com.jakewharton.rxbinding2.internal.CALLABLE_ALWAYS_TRUE
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable
import java.util.concurrent.Callable

import com.jakewharton.rxbinding2.internal.checkMainThread

/**
 * Create an observable which emits on `view` long-click events. The emitted value is
 * unspecified and should only be used as notification.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [View.setOnLongClickListener] to observe
 * long clicks. Only one observable can be used for a view at a time.
 *
 * @param handled Predicate invoked each occurrence to determine the return value of the
 * underlying [View.OnLongClickListener].
 */
@CheckResult
@JvmOverloads
fun View.longClicks(handled: Callable<Boolean> = CALLABLE_ALWAYS_TRUE): Observable<Any> {
  return ViewLongClickObservable(this, handled)
}

private class ViewLongClickObservable(
  private val view: View,
  private val handled: Callable<Boolean>
) : Observable<Any>() {

  override fun subscribeActual(observer: Observer<in Any>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, handled, observer)
    observer.onSubscribe(listener)
    view.setOnLongClickListener(listener)
  }

  private class Listener(
    private val view: View,
    private val handled: Callable<Boolean>,
    private val observer: Observer<in Any>
  ) : MainThreadDisposable(), OnLongClickListener {

    override fun onLongClick(v: View): Boolean {
      if (!isDisposed) {
        try {
          if (handled.call()) {
            observer.onNext(Unit)
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
      view.setOnLongClickListener(null)
    }
  }
}
