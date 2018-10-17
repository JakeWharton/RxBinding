@file:JvmName("RxActionMenuView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.appcompat

import android.view.MenuItem
import androidx.annotation.CheckResult
import androidx.appcompat.widget.ActionMenuView
import androidx.appcompat.widget.ActionMenuView.OnMenuItemClickListener
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

import com.jakewharton.rxbinding3.internal.checkMainThread

/**
 * Create an observable which emits the clicked menu item in `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`.
 * Unsubscribe to free this reference.
 */
@CheckResult
fun ActionMenuView.itemClicks(): Observable<MenuItem> {
  return ActionMenuViewItemClickObservable(this)
}

private class ActionMenuViewItemClickObservable(
  private val view: ActionMenuView
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
    private val actionMenuView: ActionMenuView,
    private val observer: Observer<in MenuItem>
  ) : MainThreadDisposable(), OnMenuItemClickListener {

    override fun onMenuItemClick(item: MenuItem): Boolean {
      if (!isDisposed) {
        observer.onNext(item)
      }
      return true
    }

    override fun onDispose() {
      actionMenuView.setOnMenuItemClickListener(null)
    }
  }
}
