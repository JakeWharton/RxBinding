@file:JvmName("RxMenuItem")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.view

import android.view.MenuItem
import android.view.MenuItem.OnMenuItemClickListener
import androidx.annotation.CheckResult
import com.jakewharton.rxbinding3.internal.AlwaysTrue
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

import com.jakewharton.rxbinding3.internal.checkMainThread

/**
 * Create an observable which emits on `menuItem` click events. The emitted value is
 * unspecified and should only be used as notification.
 *
 * *Warning:* The created observable keeps a strong reference to `menuItem`.
 * Unsubscribe to free this reference.
 *
 * *Warning:* The created observable uses [MenuItem.setOnMenuItemClickListener] to
 * observe clicks. Only one observable can be used for a menu item at a time.
 *
 * @param handled Function invoked with each value to determine the return value of the
 * underlying [MenuItem.OnMenuItemClickListener].
 */
@CheckResult
@JvmOverloads
fun MenuItem.clicks(handled: (MenuItem) -> Boolean = AlwaysTrue): Observable<Unit> {
  return MenuItemClickObservable(this, handled)
}

private class MenuItemClickObservable(
  private val menuItem: MenuItem,
  private val handled: (MenuItem) -> Boolean
) : Observable<Unit>() {

  override fun subscribeActual(observer: Observer<in Unit>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(menuItem, handled, observer)
    observer.onSubscribe(listener)
    menuItem.setOnMenuItemClickListener(listener)
  }

  private class Listener(
    private val menuItem: MenuItem,
    private val handled: (MenuItem) -> Boolean,
    private val observer: Observer<in Unit>
  ) : MainThreadDisposable(), OnMenuItemClickListener {

    override fun onMenuItemClick(item: MenuItem): Boolean {
      if (!isDisposed) {
        try {
          if (handled(menuItem)) {
            observer.onNext(Unit)
            return true
          }
        } catch (e: Exception) {
          observer.onError(e)
          dispose()
        }

      }
      return false
    }

    override fun onDispose() {
      menuItem.setOnMenuItemClickListener(null)
    }
  }
}
