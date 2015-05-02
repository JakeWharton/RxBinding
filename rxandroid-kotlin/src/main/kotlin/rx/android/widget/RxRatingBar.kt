package rx.android.widget

import android.widget.RatingBar
import rx.Observable
import rx.functions.Action1

/**
 * Create an observable of the rating changes on {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
public inline fun RatingBar.ratingChanges(): Observable<Float> = RxRatingBar.ratingChanges(this)

/**
 * Create an observable of the rating change events on {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
public inline fun RatingBar.ratingChangeEvents(): Observable<RatingBarChangeEvent> = RxRatingBar.ratingChangeEvents(this)

/**
 * An action which sets the rating of {@code view}.
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
public inline fun RatingBar.rating(): Action1<in Float> = RxRatingBar.setRating(this)

/**
 * An action which sets whether {@code view} is an indicator (thus non-changeable by the user).
 * <p>
 * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
 * to free this reference.
 */
public inline fun RatingBar.isIndicator(): Action1<in Boolean> = RxRatingBar.setIsIndicator(this)
