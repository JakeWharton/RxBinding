package com.jakewharton.rxbinding2.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.RatingBar;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

public final class RxRatingBar {
  /**
   * Create an observable of the rating changes on {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Note:</em> A value will be emitted immediately on subscribe.
   */
  @CheckResult @NonNull
  public static Observable<Float> ratingChanges(@NonNull RatingBar view) {
    checkNotNull(view, "view == null");
    return new RatingBarRatingChangeObservable(view);
  }

  /**
   * Create an observable of the rating change events on {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   * <p>
   * <em>Note:</em> A value will be emitted immediately on subscribe.
   */
  @CheckResult @NonNull
  public static Observable<RatingBarChangeEvent> ratingChangeEvents(@NonNull RatingBar view) {
    checkNotNull(view, "view == null");
    return new RatingBarRatingChangeEventObservable(view);
  }

  /**
   * An action which sets the rating of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Consumer<? super Float> rating(@NonNull final RatingBar view) {
    checkNotNull(view, "view == null");
    return new Consumer<Float>() {
      @Override public void accept(Float value) {
        view.setRating(value);
      }
    };
  }

  /**
   * An action which sets whether {@code view} is an indicator (thus non-changeable by the user).
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  @CheckResult @NonNull
  public static Consumer<? super Boolean> isIndicator(@NonNull final RatingBar view) {
    checkNotNull(view, "view == null");
    return new Consumer<Boolean>() {
      @Override public void accept(Boolean value) {
        view.setIsIndicator(value);
      }
    };
  }

  private RxRatingBar() {
    throw new AssertionError("No instances.");
  }
}
