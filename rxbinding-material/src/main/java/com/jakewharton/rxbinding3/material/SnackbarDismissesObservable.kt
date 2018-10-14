@file:JvmName("RxSnackbar")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.material

import androidx.annotation.CheckResult
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.Callback
import com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

/**
 * Create an observable which emits the dismiss events from `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
fun Snackbar.dismisses(): Observable<Int> {
  return SnackbarDismissesObservable(this)
}

private class SnackbarDismissesObservable(
  private val view: Snackbar
) : Observable<Int>() {

  override fun subscribeActual(observer: Observer<in Int>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.addCallback(listener.callback)
  }

  private class Listener(
    private val snackbar: Snackbar,
    observer: Observer<in Int>
  ) : MainThreadDisposable() {
    val callback = object : Callback() {
      override fun onDismissed(snackbar: Snackbar?, event: Int) {
        if (!isDisposed) {
          observer.onNext(event)
        }
      }
    }

    override fun onDispose() {
      snackbar.removeCallback(callback)
    }
  }
}
