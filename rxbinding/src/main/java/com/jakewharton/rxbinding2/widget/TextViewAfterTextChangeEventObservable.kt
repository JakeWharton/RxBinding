@file:JvmName("RxTextView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding2.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.annotation.CheckResult
import com.jakewharton.rxbinding2.InitialValueObservable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

/**
 * Create an observable of after text change events for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe using
 * [TextView.getEditableText].
 */
@CheckResult
fun TextView.afterTextChangeEvents(): InitialValueObservable<TextViewAfterTextChangeEvent> {
  return TextViewAfterTextChangeEventObservable(this)
}

/**
 * An after text-change event on a view.
 *
 * **Warning:** Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated [Context].
 */
data class TextViewAfterTextChangeEvent(
  /** The view from which this event occurred.  */
  val view: TextView,
  val editable: Editable?
)

private class TextViewAfterTextChangeEventObservable(
  private val view: TextView
) : InitialValueObservable<TextViewAfterTextChangeEvent>() {

  override fun subscribeListener(observer: Observer<in TextViewAfterTextChangeEvent>) {
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.addTextChangedListener(listener)
  }

  override val initialValue get() = TextViewAfterTextChangeEvent(view, view.editableText)

  private class Listener(
    private val view: TextView,
    private val observer: Observer<in TextViewAfterTextChangeEvent>
  ) : MainThreadDisposable(), TextWatcher {

    override fun beforeTextChanged(
      charSequence: CharSequence,
      start: Int,
      count: Int,
      after: Int
    ) {
    }

    override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable) {
      observer.onNext(TextViewAfterTextChangeEvent(view, s))
    }

    override fun onDispose() {
      view.removeTextChangedListener(this)
    }
  }
}
