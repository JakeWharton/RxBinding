@file:JvmName("RxAdapterView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.widget

import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.AdapterView.OnItemLongClickListener
import androidx.annotation.CheckResult
import com.jakewharton.rxbinding3.internal.AlwaysTrue
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

import com.jakewharton.rxbinding3.internal.checkMainThread

/**
 * Create an observable of the item long-click events for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * @param handled Function invoked with each value to determine the return value of the
 * underlying [AdapterView.OnItemLongClickListener].
 */
@CheckResult
@JvmOverloads
fun <T : Adapter> AdapterView<T>.itemLongClickEvents(
  handled: (AdapterViewItemLongClickEvent) -> Boolean = AlwaysTrue
): Observable<AdapterViewItemLongClickEvent> {
  return AdapterViewItemLongClickEventObservable(this, handled)
}

data class AdapterViewItemLongClickEvent(
  /** The view from which this event occurred.  */
  val view: AdapterView<*>,
  val clickedView: View?,
  val position: Int,
  val id: Long
)

private class AdapterViewItemLongClickEventObservable(
  private val view: AdapterView<*>,
  private val handled: (AdapterViewItemLongClickEvent) -> Boolean
) : Observable<AdapterViewItemLongClickEvent>() {

  override fun subscribeActual(observer: Observer<in AdapterViewItemLongClickEvent>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer, handled)
    observer.onSubscribe(listener)
    view.onItemLongClickListener = listener
  }

  private class Listener(
    private val view: AdapterView<*>,
    private val observer: Observer<in AdapterViewItemLongClickEvent>,
    private val handled: (AdapterViewItemLongClickEvent) -> Boolean
  ) : MainThreadDisposable(), OnItemLongClickListener {

    override fun onItemLongClick(
      parent: AdapterView<*>,
      view: View?,
      position: Int,
      id: Long
    ): Boolean {
      if (!isDisposed) {
        val event = AdapterViewItemLongClickEvent(parent, view, position, id)
        try {
          if (handled(event)) {
            observer.onNext(event)
            return true
          }
        } catch (e: Exception) {
          observer.onError(e)
          dispose()
        }

      }
      return false
    }

    override fun onDispose() {
      view.onItemLongClickListener = null
    }
  }
}
