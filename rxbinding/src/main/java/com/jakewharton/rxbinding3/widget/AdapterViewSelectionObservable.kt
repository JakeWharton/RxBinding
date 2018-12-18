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
 * Create an observable of selection events for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
fun <T : Adapter> AdapterView<T>.selectionEvents(): InitialValueObservable<AdapterViewSelectionEvent> {
  return AdapterViewSelectionObservable(this)
}

private class AdapterViewSelectionObservable(
  private val view: AdapterView<*>
) : InitialValueObservable<AdapterViewSelectionEvent>() {

  override fun subscribeListener(observer: Observer<in AdapterViewSelectionEvent>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    view.onItemSelectedListener = listener
    observer.onSubscribe(listener)
  }

  override val initialValue: AdapterViewSelectionEvent
    get() {
      val selectedPosition = view.selectedItemPosition
      return if (selectedPosition == INVALID_POSITION) {
        AdapterViewNothingSelectionEvent(view)
      } else {
        val selectedView = view.selectedView
        val selectedId = view.selectedItemId
        AdapterViewItemSelectionEvent(view, selectedView, selectedPosition, selectedId)
      }
    }

  private class Listener(
    private val view: AdapterView<*>,
    private val observer: Observer<in AdapterViewSelectionEvent>
  ) : MainThreadDisposable(), OnItemSelectedListener {

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
      if (!isDisposed) {
        observer.onNext(AdapterViewItemSelectionEvent(parent, view, position, id))
      }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
      if (!isDisposed) {
        observer.onNext(AdapterViewNothingSelectionEvent(parent))
      }
    }

    override fun onDispose() {
      view.onItemSelectedListener = null
    }
  }
}
