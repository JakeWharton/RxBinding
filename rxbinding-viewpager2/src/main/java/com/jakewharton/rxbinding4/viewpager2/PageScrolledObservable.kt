@file:JvmName("RxViewPager2")
@file:JvmMultifileClass

package com.jakewharton.rxbinding4.viewpager2

import androidx.annotation.CheckResult
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.jakewharton.rxbinding4.internal.checkMainThread
import io.reactivex.rxjava3.android.MainThreadDisposable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer

/**
 * Create an observable of page scroll events on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
fun ViewPager2.pageScrollEvents(): Observable<PageScrollEvent> =
  PageScrolledObservable(this)

data class PageScrollEvent(
  val viewPager2: ViewPager2,
  val position: Int,
  val positionOffset: Float,
  val positionOffsetPixels: Int
)

private class PageScrolledObservable(private val viewPager2: ViewPager2) : Observable<PageScrollEvent>() {

  override fun subscribeActual(observer: Observer<in PageScrollEvent>) {
    if (checkMainThread(observer)) {
      RxPageChangeCallback(viewPager2, observer).run {
        observer.onSubscribe(disposable)
        viewPager2.registerOnPageChangeCallback(this)
      }
    }
  }

  private class RxPageChangeCallback(
    private val viewPager2: ViewPager2,
    private val observer: Observer<in PageScrollEvent>
  ) : OnPageChangeCallback() {

    val disposable = object : MainThreadDisposable() {
      override fun onDispose() {
        viewPager2.unregisterOnPageChangeCallback(this@RxPageChangeCallback)
      }
    }

    override fun onPageScrolled(
      position: Int,
      positionOffset: Float,
      positionOffsetPixels: Int
    ) {
      if (!disposable.isDisposed) {
        observer.onNext(
            PageScrollEvent(
                viewPager2 = viewPager2,
                position = position,
                positionOffset = positionOffset,
                positionOffsetPixels = positionOffsetPixels
            )
        )
      }
    }
  }
}
