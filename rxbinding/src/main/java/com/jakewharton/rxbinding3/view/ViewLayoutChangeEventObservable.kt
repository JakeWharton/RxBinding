@file:JvmName("RxView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.view

import android.content.Context
import android.view.View
import android.view.View.OnLayoutChangeListener
import androidx.annotation.CheckResult
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

import com.jakewharton.rxbinding3.internal.checkMainThread

/**
 * Create an observable of layout-change events for `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
fun View.layoutChangeEvents(): Observable<ViewLayoutChangeEvent> {
  return ViewLayoutChangeEventObservable(this)
}

/**
 * A layout-change event on a view.
 *
 * **Warning:** Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated [Context].
 */
data class ViewLayoutChangeEvent(
  /** The view from which this event occurred. */
  val view: View,
  val left: Int,
  val top: Int,
  val right: Int,
  val bottom: Int,
  val oldLeft: Int,
  val oldTop: Int,
  val oldRight: Int,
  val oldBottom: Int
)

/**
 * A scroll-change event on a view.
 *
 * **Warning:** Instances keep a strong reference to the view. Operators that cache
 * instances have the potential to leak the associated [android.content.Context].
 */
data class ViewScrollChangeEvent(
  /** The view from which this event occurred.  */
  val view: View,
  val scrollX: Int,
  val scrollY: Int,
  val oldScrollX: Int,
  val oldScrollY: Int
)

private class ViewLayoutChangeEventObservable(
  private val view: View
) : Observable<ViewLayoutChangeEvent>() {

  override fun subscribeActual(observer: Observer<in ViewLayoutChangeEvent>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.addOnLayoutChangeListener(listener)
  }

  private class Listener(
    private val view: View,
    private val observer: Observer<in ViewLayoutChangeEvent>
  ) : MainThreadDisposable(), OnLayoutChangeListener {

    override fun onLayoutChange(
      v: View, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int,
      oldTop: Int, oldRight: Int, oldBottom: Int
    ) {
      if (!isDisposed) {
        observer.onNext(
            ViewLayoutChangeEvent(v, left, top, right, bottom, oldLeft, oldTop, oldRight,
                oldBottom))
      }
    }

    override fun onDispose() {
      view.removeOnLayoutChangeListener(this)
    }
  }
}
