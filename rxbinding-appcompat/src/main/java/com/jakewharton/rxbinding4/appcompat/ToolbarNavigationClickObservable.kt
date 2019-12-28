@file:JvmName("RxToolbar")
@file:JvmMultifileClass

package com.jakewharton.rxbinding4.appcompat

import android.view.View
import androidx.annotation.CheckResult
import androidx.appcompat.widget.Toolbar
import com.jakewharton.rxbinding4.internal.checkMainThread
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

/**
 * Create an observable which emits on `view` navigation click events. The emitted value is
 * unspecified and should only be used as notification.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [Toolbar.setNavigationOnClickListener]
 * to observe clicks. Only one observable can be used for a view at a time.
 */
@CheckResult
fun Toolbar.navigationClicks(): Observable<Unit> {
  return ToolbarNavigationClickObservable(this)
}

private class ToolbarNavigationClickObservable(
  private val view: Toolbar
) : Observable<Unit>() {

  override fun subscribeActual(observer: Observer<in Unit>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.setNavigationOnClickListener(listener)
  }

  private class Listener(
    private val toolbar: Toolbar,
    private val observer: Observer<in Unit>
  ) : MainThreadDisposable(), View.OnClickListener {

    override fun onClick(view: View) {
      if (!isDisposed) {
        observer.onNext(Unit)
      }
    }

    override fun onDispose() {
      toolbar.setNavigationOnClickListener(null)
    }
  }
}
