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
 * Create an observable of the rating change events on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
fun RatingBar.ratingChangeEvents(): InitialValueObservable<RatingBarChangeEvent> {
  return RatingBarRatingChangeEventObservable(this)
}

data class RatingBarChangeEvent(
  /** The view from which this event occurred.  */
  val view: RatingBar,
  val rating: Float,
  val fromUser: Boolean
)

private class RatingBarRatingChangeEventObservable(
  private val view: RatingBar
) : InitialValueObservable<RatingBarChangeEvent>() {

  override fun subscribeListener(observer: Observer<in RatingBarChangeEvent>) {
    if (!checkMainThread(observer)) {
      return
    }
    val listener = Listener(view, observer)
    view.onRatingBarChangeListener = listener
    observer.onSubscribe(listener)
  }

  override val initialValue get() = RatingBarChangeEvent(view, view.rating, false)

  private class Listener(
    private val view: RatingBar,
    private val observer: Observer<in RatingBarChangeEvent>
  ) : MainThreadDisposable(), OnRatingBarChangeListener {

    override fun onRatingChanged(ratingBar: RatingBar, rating: Float, fromUser: Boolean) {
      if (!isDisposed) {
        observer.onNext(RatingBarChangeEvent(ratingBar, rating, fromUser))
      }
    }

    override fun onDispose() {
      view.onRatingBarChangeListener = null
    }
  }
}
