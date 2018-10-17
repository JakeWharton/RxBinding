@file:JvmName("RxPopupMenu")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.appcompat

import androidx.annotation.CheckResult
import androidx.appcompat.widget.PopupMenu
import com.jakewharton.rxbinding3.internal.checkMainThread
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

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
    observer.onSubscribe(listener)
    view.setOnDismissListener(listener)
  }

  private class Listener(
    private val popupMenu: PopupMenu,
    private val observer: Observer<in Any>
  ) : MainThreadDisposable(), PopupMenu.OnDismissListener {

    override fun onDismiss(menu: PopupMenu) {
      if (!isDisposed) {
        observer.onNext(Unit)
      }
    }

    override fun onDispose() {
      popupMenu.setOnDismissListener(null)
    }
  }
}
