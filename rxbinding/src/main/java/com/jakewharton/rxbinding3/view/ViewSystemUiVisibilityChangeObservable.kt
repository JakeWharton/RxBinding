@file:JvmName("RxView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.view

import android.view.View
import android.view.View.OnSystemUiVisibilityChangeListener
import androidx.annotation.CheckResult
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

import com.jakewharton.rxbinding3.internal.checkMainThread

/**
 * Create an observable of integers representing a new system UI visibility for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses
 * [View.setOnSystemUiVisibilityChangeListener] to observe system UI visibility changes.
 * Only one observable can be used for a view at a time.
 */
@CheckResult
fun View.systemUiVisibilityChanges(): Observable<Int> {
  return ViewSystemUiVisibilityChangeObservable(this)
}

private class ViewSystemUiVisibilityChangeObservable(
  private val view: View
) : Observable<Int>() {

  override fun subscribeActual(observer: Observer<in Int>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.setOnSystemUiVisibilityChangeListener(listener)
  }

  private class Listener(
    private val view: View,
    private val observer: Observer<in Int>
  ) : MainThreadDisposable(), OnSystemUiVisibilityChangeListener {

    override fun onSystemUiVisibilityChange(visibility: Int) {
      if (!isDisposed) {
        observer.onNext(visibility)
      }
    }

    override fun onDispose() {
      view.setOnSystemUiVisibilityChangeListener(null)
    }
  }
}
