@file:JvmName("RxMenuItem")
@file:JvmMultifileClass

package com.jakewharton.rxbinding2.view

import android.view.MenuItem
import android.view.MenuItem.OnActionExpandListener
import androidx.annotation.CheckResult
import com.jakewharton.rxbinding2.internal.PREDICATE_ALWAYS_TRUE
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable
import io.reactivex.functions.Predicate

import com.jakewharton.rxbinding2.internal.checkMainThread

/**
 * Create an observable of action view events for `menuItem`.
 *
 * *Warning:* The created observable keeps a strong reference to `menuItem`.
 * Unsubscribe to free this reference.
 *
 * *Warning:* The created observable uses [MenuItem.setOnActionExpandListener] to
 * observe action view events. Only one observable can be used for a menu item at a time.
 *
 * @param handled Function invoked with each value to determine the return value of the
 * underlying [MenuItem.OnActionExpandListener].
 */
@CheckResult
@JvmOverloads
fun MenuItem.actionViewEvents(
  handled: Predicate<in MenuItemActionViewEvent> = PREDICATE_ALWAYS_TRUE
): Observable<MenuItemActionViewEvent> {
  return MenuItemActionViewEventObservable(this, handled)
}

private class MenuItemActionViewEventObservable(
  private val menuItem: MenuItem,
  private val handled: Predicate<in MenuItemActionViewEvent>
) : Observable<MenuItemActionViewEvent>() {

  override fun subscribeActual(observer: Observer<in MenuItemActionViewEvent>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(menuItem, handled, observer)
    observer.onSubscribe(listener)
    menuItem.setOnActionExpandListener(listener)
  }

  private class Listener(
    private val menuItem: MenuItem,
    private val handled: Predicate<in MenuItemActionViewEvent>,
    private val observer: Observer<in MenuItemActionViewEvent>
  ) : MainThreadDisposable(), OnActionExpandListener {

    override fun onMenuItemActionExpand(item: MenuItem): Boolean {
      return onEvent(MenuItemActionViewExpandEvent(item))
    }

    override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
      return onEvent(MenuItemActionViewCollapseEvent(item))
    }

    private fun onEvent(event: MenuItemActionViewEvent): Boolean {
      if (!isDisposed) {
        try {
          if (handled.test(event)) {
            observer.onNext(event)
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
      menuItem.setOnActionExpandListener(null)
    }
  }
}
