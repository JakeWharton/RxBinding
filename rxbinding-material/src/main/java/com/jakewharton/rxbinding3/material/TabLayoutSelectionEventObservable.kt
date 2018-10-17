@file:JvmName("RxTabLayout")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.material

import androidx.annotation.CheckResult
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import com.jakewharton.rxbinding3.internal.checkMainThread
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

/**
 * Create an observable which emits selection, reselection, and unselection events for the tabs
 * in `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* If a tab is already selected, an event will be emitted immediately on subscribe.
 */
@CheckResult
fun TabLayout.selectionEvents(): Observable<TabLayoutSelectionEvent> {
  return TabLayoutSelectionEventObservable(this)
}

private class TabLayoutSelectionEventObservable(
  val view: TabLayout
) : Observable<TabLayoutSelectionEvent>() {

  override fun subscribeActual(observer: Observer<in TabLayoutSelectionEvent>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.addOnTabSelectedListener(listener)

    val index = view.selectedTabPosition
    if (index != -1) {
      observer.onNext(TabLayoutSelectionSelectedEvent(view, view.getTabAt(index)!!))
    }
  }

  private class Listener(
    private val view: TabLayout,
    private val observer: Observer<in TabLayoutSelectionEvent>
  ) : MainThreadDisposable(), TabLayout.OnTabSelectedListener {

    override fun onTabSelected(tab: Tab) {
      if (!isDisposed) {
        observer.onNext(TabLayoutSelectionSelectedEvent(view, tab))
      }
    }

    override fun onTabUnselected(tab: Tab) {
      if (!isDisposed) {
        observer.onNext(TabLayoutSelectionUnselectedEvent(view, tab))
      }
    }

    override fun onTabReselected(tab: Tab) {
      if (!isDisposed) {
        observer.onNext(TabLayoutSelectionReselectedEvent(view, tab))
      }
    }

    override fun onDispose() {
      view.removeOnTabSelectedListener(this)
    }
  }
}
