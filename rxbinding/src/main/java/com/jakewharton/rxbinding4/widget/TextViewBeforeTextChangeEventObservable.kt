@file:JvmName("RxTextView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding4.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.annotation.CheckResult
import com.jakewharton.rxbinding4.InitialValueObservable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

/**
 * Create an observable of before text change events for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
fun TextView.beforeTextChangeEvents(): InitialValueObservable<TextViewBeforeTextChangeEvent> {
  return TextViewBeforeTextChangeEventObservable(this)
}

/**
 * A before text-change event on a view.
 *
 * **Warning:** Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated [Context].
 */
data class TextViewBeforeTextChangeEvent(
  /** The view from which this event occurred.  */
  val view: TextView,
  val text: CharSequence,
  val start: Int,
  val count: Int,
  val after: Int
)

private class TextViewBeforeTextChangeEventObservable(
  private val view: TextView
) : InitialValueObservable<TextViewBeforeTextChangeEvent>() {

  override fun subscribeListener(observer: Observer<in TextViewBeforeTextChangeEvent>) {
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.addTextChangedListener(listener)
  }

  override val initialValue get() = TextViewBeforeTextChangeEvent(view, view.text, 0, 0, 0)

  private class Listener(
    private val view: TextView,
    private val observer: Observer<in TextViewBeforeTextChangeEvent>
  ) : MainThreadDisposable(), TextWatcher {

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
      if (!isDisposed) {
        observer.onNext(TextViewBeforeTextChangeEvent(view, s, start, count, after))
      }
    }

    override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable) {
    }

    override fun onDispose() {
      view.removeTextChangedListener(this)
    }
  }
}
