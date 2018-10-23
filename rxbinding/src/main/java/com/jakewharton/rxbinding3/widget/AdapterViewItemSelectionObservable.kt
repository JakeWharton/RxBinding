@file:JvmName("RxAdapterView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.widget

import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import com.jakewharton.rxbinding3.InitialValueObservable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

import android.widget.AdapterView.INVALID_POSITION
import androidx.annotation.CheckResult
import com.jakewharton.rxbinding3.internal.checkMainThread

/**
 * Create an observable of the selected position of `view`. If nothing is selected,
 * [AdapterView.INVALID_POSITION] will be emitted.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
fun <T : Adapter> AdapterView<T>.itemSelections(): InitialValueObservable<Int> {
  return AdapterViewItemSelectionObservable(this)
}

private class AdapterViewItemSelectionObservable(
  private val view: AdapterView<*>
) : InitialValueObservable<Int>() {

  override fun subscribeListener(observer: Observer<in Int>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    view.onItemSelectedListener = listener
    observer.onSubscribe(listener)
  }

  override val initialValue get() = view.selectedItemPosition

  private class Listener(
    private val view: AdapterView<*>,
    private val observer: Observer<in Int>
  ) : MainThreadDisposable(), OnItemSelectedListener {

    override fun onItemSelected(adapterView: AdapterView<*>, view: View?, position: Int, id: Long) {
      if (!isDisposed) {
        observer.onNext(position)
      }
    }

    override fun onNothingSelected(adapterView: AdapterView<*>) {
      if (!isDisposed) {
        observer.onNext(INVALID_POSITION)
      }
    }

    override fun onDispose() {
      view.onItemSelectedListener = null
    }
  }
}
