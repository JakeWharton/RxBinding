@file:JvmName("RxPopupMenu")
@file:JvmMultifileClass

package com.jakewharton.rxbinding4.appcompat

import android.view.MenuItem
import androidx.annotation.CheckResult
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener
import com.jakewharton.rxbinding4.internal.checkMainThread
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

/**
 * Create an observable which emits the clicked item in `view`'s menu.
 *
 * *Warning:* The created observable keeps a strong reference to `view`.
 * Unsubscribe to free this reference.
 *
 * *Warning:* The created observable uses [PopupMenu.setOnMenuItemClickListener]
 * to observe dismiss change. Only one observable can be used for a view at a time.
 */
@CheckResult
fun PopupMenu.itemClicks(): Observable<MenuItem> {
  return PopupMenuItemClickObservable(this)
}

private class PopupMenuItemClickObservable(
  private val view: PopupMenu
) : Observable<MenuItem>() {

  override fun subscribeActual(observer: Observer<in MenuItem>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.setOnMenuItemClickListener(listener)
  }

  private class Listener(
    private val popupMenu: PopupMenu,
    private val observer: Observer<in MenuItem>
  ) : MainThreadDisposable(), OnMenuItemClickListener {

    override fun onMenuItemClick(item: MenuItem): Boolean {
      if (!isDisposed) {
        observer.onNext(item)
      }
      return true
    }

    override fun onDispose() {
      popupMenu.setOnMenuItemClickListener(null)
    }
  }
}
