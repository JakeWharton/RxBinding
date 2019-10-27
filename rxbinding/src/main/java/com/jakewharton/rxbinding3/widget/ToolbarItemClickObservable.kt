@file:JvmName("RxToolbar")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.widget

import androidx.annotation.RequiresApi
import android.view.MenuItem
import android.widget.Toolbar
import android.widget.Toolbar.OnMenuItemClickListener
import androidx.annotation.CheckResult
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

import com.jakewharton.rxbinding3.internal.checkMainThread

/**
 * Create an observable which emits the clicked item in `view`'s menu.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
@RequiresApi(21)
fun Toolbar.itemClicks(): Observable<MenuItem> {
  return ToolbarItemClickObservable(this)
}

@RequiresApi(21)
private class ToolbarItemClickObservable(
  private val view: Toolbar
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
    private val view: Toolbar,
    private val observer: Observer<in MenuItem>
  ) : MainThreadDisposable(), OnMenuItemClickListener {

    override fun onMenuItemClick(item: MenuItem): Boolean {
      if (!isDisposed) {
        observer.onNext(item)
        return true
      }
      return false
    }

    override fun onDispose() {
      view.setOnMenuItemClickListener(null)
    }
  }
}
