@file:JvmName("RxView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.view

import android.view.View
import android.view.View.OnClickListener
import androidx.annotation.CheckResult
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

import com.jakewharton.rxbinding3.internal.checkMainThread

/**
 * Create an observable which emits on `view` click events. The emitted value is
 * unspecified and should only be used as notification.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [View.setOnClickListener] to observe
 * clicks. Only one observable can be used for a view at a time.
 */
@CheckResult
fun View.clicks(): Observable<Unit> {
  return ViewClickObservable(this)
}

private class ViewClickObservable(
  private val view: View
) : Observable<Unit>() {

  private val listeners = OnClickListeners()

  init {
    view.setOnClickListener(listeners)
  }

  override fun subscribeActual(observer: Observer<in Unit>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, listeners, observer)
    observer.onSubscribe(listener)
    listeners.add(listener)
  }

  private class Listener(
    private val view: View,
    private val listeners: OnClickListeners,
    private val observer: Observer<in Unit>
  ) : MainThreadDisposable(), OnClickListener {

    override fun onClick(v: View) {
      if (!isDisposed) {
        observer.onNext(Unit)
      }
    }

    override fun onDispose() {
      listeners.remove(this)
      if (listeners.isEmpty()) {
        view.setOnClickListener(null)
      }
    }
  }

  private class OnClickListeners(
    private val listeners: MutableCollection<OnClickListener> = mutableListOf()
  ) : OnClickListener {
    override fun onClick(v: View?) {
      for (listener in listeners) {
        listener.onClick(v)
      }
    }

    fun add(listener: OnClickListener) {
      listeners.add(listener)
    }

    fun remove(listener: OnClickListener) {
      listeners.remove(listener)
    }

    fun isEmpty(): Boolean {
      return listeners.isEmpty()
    }
  }
}
