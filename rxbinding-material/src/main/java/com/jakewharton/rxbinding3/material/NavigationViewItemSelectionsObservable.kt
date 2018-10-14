@file:JvmName("RxNavigationView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.material

import android.view.MenuItem
import androidx.annotation.CheckResult
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

/**
 * Create an observable which emits the selected item in `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* If an item is already selected, it will be emitted immediately on subscribe.
 * This behavior assumes but does not enforce that the items are exclusively checkable.
 */
@CheckResult
fun NavigationView.itemSelections(): Observable<MenuItem> {
  return NavigationViewItemSelectionsObservable(this)
}

private class NavigationViewItemSelectionsObservable(
  private val view: NavigationView
) : Observable<MenuItem>() {

  override fun subscribeActual(observer: Observer<in MenuItem>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.setNavigationItemSelectedListener(listener)

    // Emit initial checked item, if one can be found.
    val menu = view.menu
    for (i in 0 until menu.size()) {
      val item = menu.getItem(i)
      if (item.isChecked) {
        observer.onNext(item)
        break
      }
    }
  }

  private class Listener(
    private val navigationView: NavigationView,
    private val observer: Observer<in MenuItem>
  ) : MainThreadDisposable(), OnNavigationItemSelectedListener {

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
      if (!isDisposed) {
        observer.onNext(item)
      }
      return true
    }

    override fun onDispose() {
      navigationView.setNavigationItemSelectedListener(null)
    }
  }
}
