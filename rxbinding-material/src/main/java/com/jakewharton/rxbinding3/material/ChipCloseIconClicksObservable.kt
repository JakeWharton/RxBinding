@file:JvmName("RxChip")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.material

import android.view.View
import android.view.View.OnClickListener
import androidx.annotation.CheckResult
import com.google.android.material.chip.Chip
import com.jakewharton.rxbinding3.internal.checkMainThread
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

/**
 * Create an observable which emits on [Chip] close icon click events. The emitted value is
 * unspecified and should only be used as notification.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [Chip.setOnCloseIconClickListener] to observe
 * clicks. Only one observable can be used for a view at a time.
 */
@CheckResult
fun Chip.closeIconClicks(): Observable<Unit> {
  return ChipCloseIconClicksObservable(this)
}

private class ChipCloseIconClicksObservable(
  private val view: Chip
) : Observable<Unit>() {

  override fun subscribeActual(observer: Observer<in Unit>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.setOnCloseIconClickListener(listener)
  }

  private class Listener(
    private val view: Chip,
    private val observer: Observer<in Unit>
  ) : MainThreadDisposable(), OnClickListener {

    override fun onClick(v: View) {
      if (!isDisposed) {
        observer.onNext(Unit)
      }
    }

    override fun onDispose() {
      view.setOnCloseIconClickListener(null)
    }
  }
}
