@file:JvmName("RxSwipeDismissBehavior")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.material

import android.view.View
import androidx.annotation.CheckResult
import androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
import com.google.android.material.behavior.SwipeDismissBehavior
import com.google.android.material.behavior.SwipeDismissBehavior.OnDismissListener
import com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

/**
 * Create an observable which emits the dismiss events from `view` on
 * [SwipeDismissBehavior].
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
fun View.dismisses(): Observable<View> {
  return SwipeDismissBehaviorObservable(this)
}

private class SwipeDismissBehaviorObservable(
  private val view: View
) : Observable<View>() {

  override fun subscribeActual(observer: Observer<in View>) {
    if (!checkMainThread(observer)) {
      return
    }
    val params = view.layoutParams as? LayoutParams
        ?: throw IllegalArgumentException("The view is not in a Coordinator Layout.")
    val behavior = params.behavior as SwipeDismissBehavior<*>?
        ?: throw IllegalStateException("There's no behavior set on this view.")

    val listener = Listener(behavior, observer)
    observer.onSubscribe(listener)
    behavior.setListener(listener)
  }

  private class Listener(
    private val swipeDismissBehavior: SwipeDismissBehavior<*>,
    private val observer: Observer<in View>
  ) : MainThreadDisposable(), OnDismissListener {

    override fun onDismiss(view: View) {
      if (!isDisposed) {
        observer.onNext(view)
      }
    }

    override fun onDragStateChanged(state: Int) {}

    override fun onDispose() {
      swipeDismissBehavior.setListener(null)
    }
  }
}
