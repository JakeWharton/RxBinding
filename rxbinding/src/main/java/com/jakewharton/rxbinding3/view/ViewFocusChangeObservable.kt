@file:JvmName("RxView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.view

import android.view.View
import android.view.View.OnFocusChangeListener
import androidx.annotation.CheckResult
import com.jakewharton.rxbinding3.InitialValueObservable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

/**
 * Create an observable of booleans representing the focus of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [View.setOnFocusChangeListener] to observe
 * focus change. Only one observable can be used for a view at a time.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
fun View.focusChanges(): InitialValueObservable<Boolean> {
  return ViewFocusChangeObservable(this)
}

private class ViewFocusChangeObservable(
  private val view: View
) : InitialValueObservable<Boolean>() {

  override val initialValue: Boolean
    get() = view.hasFocus()

  override fun subscribeListener(observer: Observer<in Boolean>) {
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.onFocusChangeListener = listener
  }

  private class Listener(
    private val view: View,
    private val observer: Observer<in Boolean>
  ) : MainThreadDisposable(), OnFocusChangeListener {

    override fun onFocusChange(v: View, hasFocus: Boolean) {
      if (!isDisposed) {
        observer.onNext(hasFocus)
      }
    }

    override fun onDispose() {
      view.onFocusChangeListener = null
    }
  }
}
