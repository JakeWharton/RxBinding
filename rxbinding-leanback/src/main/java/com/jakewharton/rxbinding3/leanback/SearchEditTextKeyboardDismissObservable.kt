@file:JvmName("RxSearchEditText")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.leanback

import androidx.annotation.CheckResult
import androidx.leanback.widget.SearchEditText
import androidx.leanback.widget.SearchEditText.OnKeyboardDismissListener
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

import com.jakewharton.rxbinding3.internal.checkMainThread

/**
 * Create an observable which emits the keyboard dismiss events from `view`.
 *
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
fun SearchEditText.keyboardDismisses(): Observable<Unit> {
  return SearchEditTextKeyboardDismissObservable(this)
}

private class SearchEditTextKeyboardDismissObservable(
  private val view: SearchEditText
) : Observable<Unit>() {

  override fun subscribeActual(observer: Observer<in Unit>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.setOnKeyboardDismissListener(listener)
  }

  private class Listener(
    private val view: SearchEditText,
    private val observer: Observer<in Unit>
  ) : MainThreadDisposable(), OnKeyboardDismissListener {

    override fun onKeyboardDismiss() {
      if (!isDisposed) {
        observer.onNext(Unit)
      }
    }

    override fun onDispose() {
      view.setOnKeyboardDismissListener(null)
    }
  }
}
