@file:JvmName("RxViewPager")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.viewpager

import androidx.annotation.CheckResult
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

import com.jakewharton.rxbinding2.internal.checkMainThread

/**
 * Create an observable of page scroll events on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
fun ViewPager.pageScrollEvents(): Observable<ViewPagerPageScrollEvent> {
  return ViewPagerPageScrolledObservable(this)
}

data class ViewPagerPageScrollEvent(
  val viewPager: ViewPager,
  val position: Int,
  val positionOffset: Float,
  val positionOffsetPixels: Int
)

private class ViewPagerPageScrolledObservable(
  private val view: ViewPager
) : Observable<ViewPagerPageScrollEvent>() {

  override fun subscribeActual(observer: Observer<in ViewPagerPageScrollEvent>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.addOnPageChangeListener(listener)
  }

  private class Listener(
    private val view: ViewPager,
    private val observer: Observer<in ViewPagerPageScrollEvent>
  ) : MainThreadDisposable(), OnPageChangeListener {

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
      if (!isDisposed) {
        val event = ViewPagerPageScrollEvent(view, position, positionOffset,
            positionOffsetPixels)
        observer.onNext(event)
      }
    }

    override fun onPageSelected(position: Int) {

    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onDispose() {
      view.removeOnPageChangeListener(this)
    }
  }
}
