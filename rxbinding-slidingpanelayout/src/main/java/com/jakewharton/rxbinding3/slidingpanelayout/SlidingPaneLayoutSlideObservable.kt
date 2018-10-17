@file:JvmName("RxSlidingPaneLayout")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.slidingpanelayout

import android.view.View
import androidx.annotation.CheckResult
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

import com.jakewharton.rxbinding3.internal.checkMainThread

/**
 * Create an observable of the slide offset of the pane of `view`
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [SlidingPaneLayout.setPanelSlideListener]
 * to observe dismiss change. Only one observable can be used for a view at a time.
 */
@CheckResult
fun SlidingPaneLayout.panelSlides(): Observable<Float> {
  return SlidingPaneLayoutSlideObservable(this)
}

private class SlidingPaneLayoutSlideObservable(
  private val view: SlidingPaneLayout
) : Observable<Float>() {

  override fun subscribeActual(observer: Observer<in Float>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.setPanelSlideListener(listener)
  }

  private class Listener(
    private val view: SlidingPaneLayout,
    private val observer: Observer<in Float>
  ) : MainThreadDisposable(), SlidingPaneLayout.PanelSlideListener {

    override fun onPanelSlide(panel: View, slideOffset: Float) {
      if (!isDisposed) {
        observer.onNext(slideOffset)
      }
    }

    override fun onPanelOpened(panel: View) {
    }

    override fun onPanelClosed(panel: View) {
    }

    override fun onDispose() {
      view.setPanelSlideListener(null)
    }
  }
}
