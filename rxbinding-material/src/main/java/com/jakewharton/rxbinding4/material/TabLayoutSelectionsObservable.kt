@file:JvmName("RxTabLayout")
@file:JvmMultifileClass

package com.jakewharton.rxbinding4.material

import androidx.annotation.CheckResult
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
import com.google.android.material.tabs.TabLayout.Tab
import com.jakewharton.rxbinding4.internal.checkMainThread
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

/**
 * Create an observable which emits the selected tab in `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* If a tab is already selected, it will be emitted immediately on subscribe.
 */
@CheckResult
fun TabLayout.selections(): Observable<Tab> {
  return TabLayoutSelectionsObservable(this)
}

private class TabLayoutSelectionsObservable(
  private val view: TabLayout
) : Observable<Tab>() {

  override fun subscribeActual(observer: Observer<in Tab>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view,
        observer)
    observer.onSubscribe(listener)
    view.addOnTabSelectedListener(listener)
    val index = view.selectedTabPosition
    if (index != -1) {
      observer.onNext(view.getTabAt(index)!!)
    }
  }

  private class Listener(
    private val tabLayout: TabLayout,
    private val observer: Observer<in Tab>
  ) : MainThreadDisposable(), BaseOnTabSelectedListener<Tab> {

    override fun onDispose() {
      tabLayout.removeOnTabSelectedListener(this)
    }

    override fun onTabSelected(tab: Tab) {
      if (!isDisposed) {
        observer.onNext(tab)
      }
    }

    override fun onTabUnselected(tab: Tab) {}

    override fun onTabReselected(tab: Tab) {}
  }
}
