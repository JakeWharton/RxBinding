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

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
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
