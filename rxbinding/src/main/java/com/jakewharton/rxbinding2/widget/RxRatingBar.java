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

  private RxRatingBar() {
    throw new AssertionError("No instances.");
  }
}
