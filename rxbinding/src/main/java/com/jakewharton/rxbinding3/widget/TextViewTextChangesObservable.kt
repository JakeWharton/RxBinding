@file:JvmName("RxTextView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.widget

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.annotation.CheckResult
import com.jakewharton.rxbinding3.InitialValueObservable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

/**
 * Create an observable of character sequences for text changes on `view`.
 *
 * *Warning:* Values emitted by this observable are **mutable** and owned by the host
 * `TextView` and thus are **not safe** to cache or delay reading (such as by observing
 * on a different thread). If you want to cache or delay reading the items emitted then you must
 * map values through a function which calls [String.valueOf] or
 * [.toString()][CharSequence.toString] to create a copy.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
fun TextView.textChanges(): InitialValueObservable<CharSequence> {
  return TextViewTextChangesObservable(this)
}

private class TextViewTextChangesObservable(
  private val view: TextView
) : InitialValueObservable<CharSequence>() {

  override fun subscribeListener(observer: Observer<in CharSequence>) {
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.addTextChangedListener(listener)
  }

  override val initialValue get() = view.text

  private class Listener(
    private val view: TextView,
    private val observer: Observer<in CharSequence>
  ) : MainThreadDisposable(), TextWatcher {

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
      if (!isDisposed) {
        observer.onNext(s)
      }
    }

    override fun afterTextChanged(s: Editable) {
    }

    override fun onDispose() {
      view.removeTextChangedListener(this)
    }
  }
}
