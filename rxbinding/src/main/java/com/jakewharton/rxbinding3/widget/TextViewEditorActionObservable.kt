@file:JvmName("RxTextView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.widget

import android.view.KeyEvent
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.annotation.CheckResult
import com.jakewharton.rxbinding3.internal.AlwaysTrue
import com.jakewharton.rxbinding3.internal.checkMainThread
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

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
fun TextView.editorActions(handled: (Int) -> Boolean = AlwaysTrue): Observable<Int> {
  return TextViewEditorActionObservable(this, handled)
}

private class TextViewEditorActionObservable(
  private val view: TextView,
  private val handled: (Int) -> Boolean
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
    private val handled: (Int) -> Boolean
  ) : MainThreadDisposable(), OnEditorActionListener {

    override fun onEditorAction(textView: TextView, actionId: Int, keyEvent: KeyEvent?): Boolean {
      try {
        if (!isDisposed && handled(actionId)) {
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
