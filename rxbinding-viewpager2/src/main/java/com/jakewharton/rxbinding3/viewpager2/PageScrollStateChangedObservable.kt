@file:JvmName("RxViewPager2")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.viewpager2

import androidx.annotation.CheckResult
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.jakewharton.rxbinding3.internal.checkMainThread
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

/**
 * Create an observable of scroll state change events on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
fun ViewPager2.pageScrollStateChanges(): Observable<Int> =
  PageScrollStateChangedObservable(this)

private class PageScrollStateChangedObservable(private val viewPager2: ViewPager2) : Observable<Int>() {

  override fun subscribeActual(observer: Observer<in Int>) {
    if (checkMainThread(observer)) {
      RxPageChangeCallback(viewPager2, observer).run {
        observer.onSubscribe(disposable)
        viewPager2.registerOnPageChangeCallback(this)
      }
    }
  }

  private class RxPageChangeCallback(
    private val viewPager2: ViewPager2,
    private val observer: Observer<in Int>
  ) : OnPageChangeCallback() {

    val disposable = object : MainThreadDisposable() {
      override fun onDispose() {
        viewPager2.unregisterOnPageChangeCallback(this@RxPageChangeCallback)
      }
    }

    override fun onPageScrollStateChanged(state: Int) {
      if (!disposable.isDisposed) {
        observer.onNext(state)
      }
    }
  }
}
