@file:JvmName("RxRadioGroup")
@file:JvmMultifileClass

package com.jakewharton.rxbinding4.widget

import android.widget.RadioGroup
import android.widget.RadioGroup.OnCheckedChangeListener
import androidx.annotation.CheckResult
import com.jakewharton.rxbinding4.InitialValueObservable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

import com.jakewharton.rxbinding4.internal.checkMainThread

/**
 * Create an observable of the checked view ID changes in `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
fun RadioGroup.checkedChanges(): InitialValueObservable<Int> {
  return RadioGroupCheckedChangeObservable(this)
}

private class RadioGroupCheckedChangeObservable(
  private val view: RadioGroup
) : InitialValueObservable<Int>() {

  override fun subscribeListener(observer: Observer<in Int>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    view.setOnCheckedChangeListener(listener)
    observer.onSubscribe(listener)
  }

  override val initialValue get() = view.checkedRadioButtonId

  private class Listener(
    private val view: RadioGroup,
    private val observer: Observer<in Int>
  ) : MainThreadDisposable(), OnCheckedChangeListener {
    private var lastChecked = -1

    override fun onCheckedChanged(radioGroup: RadioGroup, checkedId: Int) {
      if (!isDisposed && checkedId != lastChecked) {
        lastChecked = checkedId
        observer.onNext(checkedId)
      }
    }

    override fun onDispose() {
      view.setOnCheckedChangeListener(null)
    }
  }
}
