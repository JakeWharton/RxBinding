@file:Suppress(
    names = "NOTHING_TO_INLINE"
)

package com.jakewharton.rxbinding2.widget

import android.support.annotation.CheckResult
import android.widget.RatingBar
import com.jakewharton.rxbinding2.InitialValueObservable
import io.reactivex.functions.Consumer

/**
 * Create an observable of the rating changes on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
inline fun RatingBar.ratingChanges(): InitialValueObservable<Float> = RxRatingBar.ratingChanges(this)

/**
 * Create an observable of the rating change events on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
inline fun RatingBar.ratingChangeEvents(): InitialValueObservable<RatingBarChangeEvent> = RxRatingBar.ratingChangeEvents(this)

/**
 * An action which sets the rating of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
inline fun RatingBar.rating(): Consumer<in Float> = RxRatingBar.rating(this)

/**
 * An action which sets whether `view` is an indicator (thus non-changeable by the user).
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
inline fun RatingBar.isIndicator(): Consumer<in Boolean> = RxRatingBar.isIndicator(this)
