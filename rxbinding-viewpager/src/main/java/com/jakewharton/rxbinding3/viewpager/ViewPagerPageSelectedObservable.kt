@file:JvmName("RxViewPager")
@file:JvmMultifileClass

package com.jakewharton.rxbinding3.viewpager

import androidx.annotation.CheckResult
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.jakewharton.rxbinding2.InitialValueObservable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

/**
 * Create an observable of page selected events on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 *  *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
fun ViewPager.pageSelections(): InitialValueObservable<Int> {
  return ViewPagerPageSelectedObservable(this)
}

private class ViewPagerPageSelectedObservable(
  private val view: ViewPager
) : InitialValueObservable<Int>() {

  override fun subscribeListener(observer: Observer<in Int>) {
    val listener = Listener(view, observer)
    observer.onSubscribe(listener)
    view.addOnPageChangeListener(listener)
  }

  override val initialValue get() = view.currentItem

  private class Listener(
    private val view: ViewPager,
    private val observer: Observer<in Int>
  ) : MainThreadDisposable(), OnPageChangeListener {

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
      if (!isDisposed) {
        observer.onNext(position)
      }
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onDispose() {
      view.removeOnPageChangeListener(this)
    }
  }
}
