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
 * Create an observable of text change events for `view`.
 *
 * *Warning:* Values emitted by this observable contain a **mutable**
 * [CharSequence] owned by the host `TextView` and thus are **not safe** to cache
 * or delay reading (such as by observing on a different thread). If you want to cache or delay
 * reading the items emitted then you must map values through a function which calls
 * [String.valueOf] or [.toString()][CharSequence.toString] to create a copy.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
fun TextView.textChangeEvents(): InitialValueObservable<TextViewTextChangeEvent> {
  return TextViewTextChangeEventObservable(this)
}

/**
 * A text-change event on a view.
 *
 * **Warning:** Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated [Context].
 */
data class TextViewTextChangeEvent(
  /** The view from which this event occurred.  */
  val view: TextView,
  val text: CharSequence,
  val start: Int,
  val before: Int,
  val count: Int
)

private class TextViewTextChangeEventObservable(
  private val view: TextView
) : InitialValueObservable<TextViewTextChangeEvent>() {

  override val initialValue get() = TextViewTextChangeEvent(view, view.text, 0, 0, 0)

  override fun subscribeListener(observer: Observer<in TextViewTextChangeEvent>) {
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.addTextChangedListener(listener)
  }

  private class Listener(
    private val view: TextView,
    private val observer: Observer<in TextViewTextChangeEvent>
  ) : MainThreadDisposable(), TextWatcher {

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
      if (!isDisposed) {
        observer.onNext(TextViewTextChangeEvent(view, s, start, before, count))
      }
    }

    override fun afterTextChanged(editable: Editable) {
    }

    override fun onDispose() {
      view.removeTextChangedListener(this)
    }
  }
}
