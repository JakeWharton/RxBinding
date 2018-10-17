@file:JvmName("RxToolbar")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.widget

import androidx.annotation.RequiresApi
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toolbar
import androidx.annotation.CheckResult
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

import com.jakewharton.rxbinding3.internal.checkMainThread

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
@RequiresApi(21)
fun Toolbar.navigationClicks(): Observable<Unit> {
  return ToolbarNavigationClickObservable(this)
}

@RequiresApi(21)
internal class ToolbarNavigationClickObservable(
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
    private val view: Toolbar,
    private val observer: Observer<in Unit>
  ) : MainThreadDisposable(), OnClickListener {

    override fun onClick(v: View) {
      if (!isDisposed) {
        observer.onNext(Unit)
      }
    }

    override fun onDispose() {
      view.setNavigationOnClickListener(null)
    }
  }
}
