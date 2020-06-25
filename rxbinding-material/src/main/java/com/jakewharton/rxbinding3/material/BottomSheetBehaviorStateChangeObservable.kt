@file:JvmName("RxBottomSheetBehavior")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.material

import android.view.View
import androidx.annotation.CheckResult
import androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.jakewharton.rxbinding3.internal.checkMainThread
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

/**
 * Create an observable which emits the state changes from `view` on
 * [BottomSheetBehavior]
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [BottomSheetBehavior.setBottomSheetCallback] to observe
 * state changes. Only one observable can be used for a view at a time.
 */
@CheckResult
fun View.stateChanges(): Observable<Int> {
  return BottomSheetBehaviorStateChangeObservable(this)
}

private class BottomSheetBehaviorStateChangeObservable(
    private val view: View
) : Observable<Int>() {

  override fun subscribeActual(observer: Observer<in Int>) {
    if (!checkMainThread(observer)) {
      return
    }
    val params = view.layoutParams as? LayoutParams
        ?: throw IllegalArgumentException("The view is not in a Coordinator Layout.")
    val behavior = params.behavior as BottomSheetBehavior<*>?
        ?: throw IllegalStateException("There's no behavior set on this view.")

    val listener = Listener(behavior, observer)
    observer.onSubscribe(listener)
    behavior.setBottomSheetCallback(listener.callback)
  }

  private class Listener(
      private val behavior: BottomSheetBehavior<*>,
      private val observer: Observer<in Int>
  ) : MainThreadDisposable() {

    val callback = object : BottomSheetCallback() {
      override fun onStateChanged(view: View, newState: Int) {
        if (!isDisposed) {
          observer.onNext(newState)
        }
      }

      override fun onSlide(view: View, slideOffset: Float) {}
    }

    override fun onDispose() {
      behavior.setBottomSheetCallback(null)
    }
  }
}