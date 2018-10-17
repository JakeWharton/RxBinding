@file:JvmName("RxPopupMenu")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.widget

import android.widget.PopupMenu
import androidx.annotation.CheckResult
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

import com.jakewharton.rxbinding3.internal.checkMainThread

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
fun PopupMenu.dismisses(): Observable<Any> {
  return PopupMenuDismissObservable(this)
}

private class PopupMenuDismissObservable(
  private val view: PopupMenu
) : Observable<Any>() {

  override fun subscribeActual(observer: Observer<in Any>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    view.setOnDismissListener(listener)
    observer.onSubscribe(listener)
  }

  private class Listener(
    private val view: PopupMenu,
    private val observer: Observer<in Any>
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
