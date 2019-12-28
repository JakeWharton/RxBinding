@file:JvmName("RxRatingBar")
@file:JvmMultifileClass

package com.jakewharton.rxbinding4.widget

import android.widget.RatingBar
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.annotation.CheckResult
import com.jakewharton.rxbinding4.InitialValueObservable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.android.MainThreadDisposable

import com.jakewharton.rxbinding4.internal.checkMainThread

/**
 * Create an observable of the rating changes on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
fun RatingBar.ratingChanges(): InitialValueObservable<Float> {
  return RatingBarRatingChangeObservable(this)
}

private class RatingBarRatingChangeObservable(
  private val view: RatingBar
) : InitialValueObservable<Float>() {

  override fun subscribeListener(observer: Observer<in Float>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    view.onRatingBarChangeListener = listener
    observer.onSubscribe(listener)
  }

  override val initialValue get() = view.rating

  private class Listener(
    private val view: RatingBar,
    private val observer: Observer<in Float>
  ) : MainThreadDisposable(), OnRatingBarChangeListener {

    override fun onRatingChanged(ratingBar: RatingBar, rating: Float, fromUser: Boolean) {
      if (!isDisposed) {
        observer.onNext(rating)
      }
    }

    override fun onDispose() {
      view.onRatingBarChangeListener = null
    }
  }
}
