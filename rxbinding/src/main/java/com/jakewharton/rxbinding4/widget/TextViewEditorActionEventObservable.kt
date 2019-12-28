@file:JvmName("RxTextView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding4.widget

import android.view.KeyEvent
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.annotation.CheckResult
import com.jakewharton.rxbinding4.internal.AlwaysTrue
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

import com.jakewharton.rxbinding4.internal.checkMainThread

/**
 * Create an observable of editor action events on `view`.
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
fun TextView.editorActionEvents(
  handled: (TextViewEditorActionEvent) -> Boolean = AlwaysTrue
): Observable<TextViewEditorActionEvent> {
  return TextViewEditorActionEventObservable(this, handled)
}

data class TextViewEditorActionEvent(
  /** The view from which this event occurred.  */
  val view: TextView,
  val actionId: Int,
  val keyEvent: KeyEvent?
)

private class TextViewEditorActionEventObservable(
  private val view: TextView,
  private val handled: (TextViewEditorActionEvent) -> Boolean
) : Observable<TextViewEditorActionEvent>() {

  override fun subscribeActual(observer: Observer<in TextViewEditorActionEvent>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer, handled)
    observer.onSubscribe(listener)
    view.setOnEditorActionListener(listener)
  }

  private class Listener(
    private val view: TextView,
    private val observer: Observer<in TextViewEditorActionEvent>,
    private val handled: (TextViewEditorActionEvent) -> Boolean
  ) : MainThreadDisposable(), OnEditorActionListener {

    override fun onEditorAction(textView: TextView, actionId: Int, keyEvent: KeyEvent?): Boolean {
      val event = TextViewEditorActionEvent(view, actionId, keyEvent)
      try {
        if (!isDisposed && handled(event)) {
          observer.onNext(event)
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
