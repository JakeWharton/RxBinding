package com.jakewharton.rxbinding.widget

import android.widget.RatingBar
import rx.Observable
import rx.functions.Action1

/**
 * Create an observable of the rating changes on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
public inline fun RatingBar.ratingChanges(): Observable<Float> = RxRatingBar.ratingChanges(this)

/**
 * Create an observable of the rating change events on `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
public inline fun RatingBar.ratingChangeEvents(): Observable<RatingBarChangeEvent> = RxRatingBar.ratingChangeEvents(this)

/**
 * An action which sets the rating of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun RatingBar.rating(): Action1<in Float> = RxRatingBar.rating(this)

/**
 * An action which sets whether `view` is an indicator (thus non-changeable by the user).
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun RatingBar.isIndicator(): Action1<in Boolean> = RxRatingBar.isIndicator(this)
