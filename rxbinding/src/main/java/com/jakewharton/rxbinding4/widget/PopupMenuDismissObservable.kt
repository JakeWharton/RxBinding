@file:JvmName("RxPopupMenu")
@file:JvmMultifileClass

package com.jakewharton.rxbinding4.widget

import android.widget.PopupMenu
import androidx.annotation.CheckResult
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

import com.jakewharton.rxbinding4.internal.checkMainThread

/**
 * Create an observable which emits on `view` dismiss events. The emitted value is
 * unspecified and should only be used as notification.
 *
 * *Warning:* The created observable keeps a strong reference to `view`.
 * Unsubscribe to free this reference.
 *
 * *Warning:* The created observable uses [PopupMenu.setOnDismissListener] to
 * observe dismiss change. Only one observable can be used for a view at a time.
 */
@CheckResult
fun PopupMenu.dismisses(): Observable<Unit> {
  return PopupMenuDismissObservable(this)
}

private class PopupMenuDismissObservable(
  private val view: PopupMenu
) : Observable<Unit>() {

  override fun subscribeActual(observer: Observer<in Unit>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    view.setOnDismissListener(listener)
    observer.onSubscribe(listener)
  }

  private class Listener(
    private val view: PopupMenu,
    private val observer: Observer<in Unit>
  ) : MainThreadDisposable(), PopupMenu.OnDismissListener {

    override fun onDismiss(popupMenu: PopupMenu) {
      if (!isDisposed) {
        observer.onNext(Unit)
      }
    }

    override fun onDispose() {
      view.setOnDismissListener(null)
    }
  }
}
