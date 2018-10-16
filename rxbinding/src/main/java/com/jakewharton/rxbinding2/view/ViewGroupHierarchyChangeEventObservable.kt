@file:JvmName("RxViewGroup")
@file:JvmMultifileClass

package com.jakewharton.rxbinding2.view

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.OnHierarchyChangeListener
import androidx.annotation.CheckResult
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

import com.jakewharton.rxbinding2.internal.checkMainThread

/**
 * Create an observable of hierarchy change events for `viewGroup`.
 *
 * *Warning:* The created observable keeps a strong reference to `viewGroup`.
 * Unsubscribe to free this reference.
 */
@CheckResult
fun ViewGroup.changeEvents(): Observable<ViewGroupHierarchyChangeEvent> {
  return ViewGroupHierarchyChangeEventObservable(this)
}

private class ViewGroupHierarchyChangeEventObservable(
  private val viewGroup: ViewGroup
) : Observable<ViewGroupHierarchyChangeEvent>() {

  override fun subscribeActual(observer: Observer<in ViewGroupHierarchyChangeEvent>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(viewGroup, observer)
    observer.onSubscribe(listener)
    viewGroup.setOnHierarchyChangeListener(listener)
  }

  private class Listener(
    private val viewGroup: ViewGroup,
    private val observer: Observer<in ViewGroupHierarchyChangeEvent>
  ) : MainThreadDisposable(), OnHierarchyChangeListener {

    override fun onChildViewAdded(parent: View, child: View) {
      if (!isDisposed) {
        observer.onNext(ViewGroupHierarchyChildViewAddEvent(viewGroup, child))
      }
    }

    override fun onChildViewRemoved(parent: View, child: View) {
      if (!isDisposed) {
        observer.onNext(ViewGroupHierarchyChildViewRemoveEvent(viewGroup, child))
      }
    }

    override fun onDispose() {
      viewGroup.setOnHierarchyChangeListener(null)
    }
  }
}
