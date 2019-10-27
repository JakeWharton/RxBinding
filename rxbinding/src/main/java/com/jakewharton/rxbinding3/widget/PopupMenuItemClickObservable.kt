@file:JvmName("RxPopupMenu")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.widget

import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.PopupMenu.OnMenuItemClickListener
import androidx.annotation.CheckResult
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

import com.jakewharton.rxbinding3.internal.checkMainThread

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
    view.setOnMenuItemClickListener(listener)
    observer.onSubscribe(listener)
  }

  private class Listener(
    private val view: PopupMenu,
    private val observer: Observer<in MenuItem>
  ) : MainThreadDisposable(), OnMenuItemClickListener {

    override fun onMenuItemClick(menuItem: MenuItem): Boolean {
      if (!isDisposed) {
        observer.onNext(menuItem)
        return true
      }
      return false
    }

    override fun onDispose() {
      view.setOnMenuItemClickListener(null)
    }
  }
}
