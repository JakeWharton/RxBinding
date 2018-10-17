@file:JvmName("RxAdapterView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding2.widget

import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.AdapterView.OnItemLongClickListener
import androidx.annotation.CheckResult
import com.jakewharton.rxbinding2.internal.CALLABLE_ALWAYS_TRUE
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable
import java.util.concurrent.Callable

import com.jakewharton.rxbinding2.internal.checkMainThread

/**
 * Create an observable of the position of item long-clicks for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * @param handled Function invoked each occurrence to determine the return value of the
 * underlying [AdapterView.OnItemLongClickListener].
 */
@CheckResult
@JvmOverloads
fun <T : Adapter> AdapterView<T>.itemLongClicks(
  handled: Callable<Boolean> = CALLABLE_ALWAYS_TRUE
): Observable<Int> {
  return AdapterViewItemLongClickObservable(this, handled)
}

private class AdapterViewItemLongClickObservable(
  private val view: AdapterView<*>,
  private val handled: Callable<Boolean>
) : Observable<Int>() {

  override fun subscribeActual(observer: Observer<in Int>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer, handled)
    observer.onSubscribe(listener)
    view.onItemLongClickListener = listener
  }

  private class Listener(
    private val view: AdapterView<*>,
    private val observer: Observer<in Int>,
    private val handled: Callable<Boolean>
  ) : MainThreadDisposable(), OnItemLongClickListener {

    override fun onItemLongClick(
      parent: AdapterView<*>,
      view: View,
      position: Int,
      id: Long
    ): Boolean {
      if (!isDisposed) {
        try {
          if (handled.call()) {
            observer.onNext(position)
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
