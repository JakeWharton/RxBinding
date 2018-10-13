package com.jakewharton.rxbinding2.widget;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import android.widget.RatingBar;
import com.jakewharton.rxbinding2.InitialValueObservable;
import io.reactivex.functions.Consumer;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkNotNull;

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
  public static InitialValueObservable<Float> ratingChanges(@NonNull RatingBar view) {
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
  public static InitialValueObservable<RatingBarChangeEvent> ratingChangeEvents(
      @NonNull RatingBar view) {
    checkNotNull(view, "view == null");
    return new RatingBarRatingChangeEventObservable(view);
  }

  /**
   * An action which sets the rating of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   *
   * @deprecated Use view::setRating method reference.
   */
  @Deprecated
  @CheckResult @NonNull
  public static Consumer<? super Float> rating(@NonNull RatingBar view) {
    checkNotNull(view, "view == null");
    return view::setRating;
  }

  /**
   * An action which sets whether {@code view} is an indicator (thus non-changeable by the user).
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   *
   * @deprecated Use view::setIsIndicator method reference.
   */
  @Deprecated
  @CheckResult @NonNull
  public static Consumer<? super Boolean> isIndicator(@NonNull RatingBar view) {
    checkNotNull(view, "view == null");
    return view::setIsIndicator;
  }

  private RxRatingBar() {
    throw new AssertionError("No instances.");
  }
}
