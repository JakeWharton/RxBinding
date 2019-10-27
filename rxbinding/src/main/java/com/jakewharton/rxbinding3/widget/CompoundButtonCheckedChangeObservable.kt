@file:JvmName("RxCompoundButton")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.widget

import android.widget.CompoundButton
import android.widget.CompoundButton.OnCheckedChangeListener
import androidx.annotation.CheckResult
import com.jakewharton.rxbinding3.InitialValueObservable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

import com.jakewharton.rxbinding3.internal.checkMainThread

/**
 * Create an observable of booleans representing the checked state of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [CompoundButton.setOnCheckedChangeListener]
 * to observe checked changes. Only one observable can be used for a view at a time.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
fun CompoundButton.checkedChanges(): InitialValueObservable<Boolean> {
  return CompoundButtonCheckedChangeObservable(this)
}

private class CompoundButtonCheckedChangeObservable(
  private val view: CompoundButton
) : InitialValueObservable<Boolean>() {

  override fun subscribeListener(observer: Observer<in Boolean>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.setOnCheckedChangeListener(listener)
  }

  override val initialValue get() = view.isChecked

  private class Listener(
    private val view: CompoundButton,
    private val observer: Observer<in Boolean>
  ) : MainThreadDisposable(), OnCheckedChangeListener {

    override fun onCheckedChanged(compoundButton: CompoundButton, isChecked: Boolean) {
      if (!isDisposed) {
        observer.onNext(isChecked)
      }
    }

    override fun onDispose() {
      view.setOnCheckedChangeListener(null)
    }
  }
}
