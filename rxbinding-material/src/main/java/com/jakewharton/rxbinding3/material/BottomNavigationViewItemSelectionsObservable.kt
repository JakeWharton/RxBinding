@file:JvmName("RxBottomNavigationView")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.material

import android.view.MenuItem
import androidx.annotation.CheckResult
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener
import com.jakewharton.rxbinding3.internal.checkMainThread
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

/**
 * Create an observable which emits the selected item in `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* If an item is already selected, it will be emitted immediately on subscribe.
 */
@CheckResult
fun BottomNavigationView.itemSelections(): Observable<MenuItem> {
  return BottomNavigationViewItemSelectionsObservable(this)
}

private class BottomNavigationViewItemSelectionsObservable(
  private val view: BottomNavigationView
) : Observable<MenuItem>() {

  override fun subscribeActual(observer: Observer<in MenuItem>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.setOnNavigationItemSelectedListener(listener)

    // Emit initial item, if one can be found
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
    private val bottomNavigationView: BottomNavigationView,
    private val observer: Observer<in MenuItem>
  ) : MainThreadDisposable(), OnNavigationItemSelectedListener {

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
      if (!isDisposed) {
        observer.onNext(item)
      }
      return true
    }

    override fun onDispose() {
      bottomNavigationView.setOnNavigationItemSelectedListener(null)
    }
  }
}
