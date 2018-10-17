@file:JvmName("RxSearchEditText")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.leanback

import androidx.annotation.CheckResult
import androidx.leanback.widget.SearchEditText
import androidx.leanback.widget.SearchEditText.OnKeyboardDismissListener
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

import com.jakewharton.rxbinding3.internal.checkMainThread

/**
 * Create an observable which emits the keyboard dismiss events from `view`.
 *
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
fun SearchEditText.keyboardDismisses(): Observable<Any> {
  return SearchEditTextKeyboardDismissOnSubscribe(this)
}

private class SearchEditTextKeyboardDismissOnSubscribe(
  private val view: SearchEditText
) : Observable<Any>() {

  override fun subscribeActual(observer: Observer<in Any>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.setOnKeyboardDismissListener(listener)
  }

  private class Listener(
    private val view: SearchEditText,
    private val observer: Observer<in Any>
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
