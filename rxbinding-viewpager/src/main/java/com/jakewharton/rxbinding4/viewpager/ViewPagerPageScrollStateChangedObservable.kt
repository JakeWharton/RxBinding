@file:JvmName("RxViewPager")
@file:JvmMultifileClass

package com.jakewharton.rxbinding4.viewpager

import androidx.annotation.CheckResult
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

import com.jakewharton.rxbinding4.internal.checkMainThread

/**
 * Create an observable of scroll state change events on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
fun ViewPager.pageScrollStateChanges(): Observable<Int> {
  return ViewPagerPageScrollStateChangedObservable(this)
}

private class ViewPagerPageScrollStateChangedObservable(
  private val view: ViewPager
) : Observable<Int>() {

  override fun subscribeActual(observer: Observer<in Int>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.addOnPageChangeListener(listener)
  }

  private class Listener(
    private val view: ViewPager,
    private val observer: Observer<in Int>
  ) : MainThreadDisposable(), OnPageChangeListener {

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
    }

    override fun onPageScrollStateChanged(state: Int) {
      if (!isDisposed) {
        observer.onNext(state)
      }
    }

    override fun onDispose() {
      view.removeOnPageChangeListener(this)
    }
  }
}
