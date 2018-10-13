@file:JvmName("RxSlidingPaneLayout")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.slidingpanelayout

import android.view.View
import androidx.annotation.CheckResult
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import androidx.slidingpanelayout.widget.SlidingPaneLayout.PanelSlideListener
import com.jakewharton.rxbinding2.InitialValueObservable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

/**
 * Create an observable of the open state of the pane of `view`
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [SlidingPaneLayout.setPanelSlideListener]
 * to observe dismiss change. Only one observable can be used for a view at a time.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
fun SlidingPaneLayout.panelOpens(): InitialValueObservable<Boolean> {
  return SlidingPaneLayoutPaneOpenedObservable(this)
}

private class SlidingPaneLayoutPaneOpenedObservable(
  private val view: SlidingPaneLayout
) : InitialValueObservable<Boolean>() {

  override fun subscribeListener(observer: Observer<in Boolean>) {
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.setPanelSlideListener(listener)
  }

  override fun getInitialValue() = view.isOpen

  private class Listener(
    private val view: SlidingPaneLayout,
    private val observer: Observer<in Boolean>
  ) : MainThreadDisposable(), PanelSlideListener {

    override fun onPanelSlide(panel: View, slideOffset: Float) {
    }

    override fun onPanelOpened(panel: View) {
      if (!isDisposed) {
        observer.onNext(true)
      }
    }

    override fun onPanelClosed(panel: View) {
      if (!isDisposed) {
        observer.onNext(false)
      }
    }

    override fun onDispose() {
      view.setPanelSlideListener(null)
    }
  }
}
