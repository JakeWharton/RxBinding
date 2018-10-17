@file:JvmName("RxTextView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding2.widget

import android.view.KeyEvent
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.annotation.CheckResult
import com.jakewharton.rxbinding2.internal.PREDICATE_ALWAYS_TRUE
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable
import io.reactivex.functions.Predicate

import com.jakewharton.rxbinding2.internal.checkMainThread

/**
 * Create an observable of editor actions on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [TextView.OnEditorActionListener] to
 * observe actions. Only one observable can be used for a view at a time.
 *
 * @param handled Predicate invoked each occurrence to determine the return value of the
 * underlying [TextView.OnEditorActionListener].
 */
@CheckResult
@JvmOverloads
fun TextView.editorActions(handled: Predicate<in Int> = PREDICATE_ALWAYS_TRUE): Observable<Int> {
  return TextViewEditorActionObservable(this, handled)
}

private class TextViewEditorActionObservable(
  private val view: TextView,
  private val handled: Predicate<in Int>
) : Observable<Int>() {

  override fun subscribeActual(observer: Observer<in Int>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer, handled)
    observer.onSubscribe(listener)
    view.setOnEditorActionListener(listener)
  }

  private class Listener(
    private val view: TextView, private val observer: Observer<in Int>,
    private val handled: Predicate<in Int>
  ) : MainThreadDisposable(), OnEditorActionListener {

    override fun onEditorAction(textView: TextView, actionId: Int, keyEvent: KeyEvent?): Boolean {
      try {
        if (!isDisposed && handled.test(actionId)) {
          observer.onNext(actionId)
          return true
        }
      } catch (e: Exception) {
        observer.onError(e)
        dispose()
      }

      return false
    }

    override fun onDispose() {
      view.setOnEditorActionListener(null)
    }
  }
}
