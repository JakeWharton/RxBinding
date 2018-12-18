@file:JvmName("RxAdapterView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.widget

import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.annotation.CheckResult
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

import com.jakewharton.rxbinding3.internal.checkMainThread

/**
 * Create an observable of the item click events for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
fun <T : Adapter> AdapterView<T>.itemClickEvents(): Observable<AdapterViewItemClickEvent> {
  return AdapterViewItemClickEventObservable(this)
}

data class AdapterViewItemClickEvent(
  /** The view from which this event occurred.  */
  val view: AdapterView<*>,
  val clickedView: View?,
  val position: Int,
  val id: Long
)

private class AdapterViewItemClickEventObservable(
  private val view: AdapterView<*>
) : Observable<AdapterViewItemClickEvent>() {

  override fun subscribeActual(observer: Observer<in AdapterViewItemClickEvent>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.onItemClickListener = listener
  }

  private class Listener(
    private val view: AdapterView<*>,
    private val observer: Observer<in AdapterViewItemClickEvent>
  ) : MainThreadDisposable(), OnItemClickListener {

    override fun onItemClick(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
      if (!isDisposed) {
        observer.onNext(AdapterViewItemClickEvent(parent, view, position, id))
      }
    }

    override fun onDispose() {
      view.onItemClickListener = null
    }
  }
}
