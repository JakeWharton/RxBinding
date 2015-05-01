package rx.android.widget;

import android.widget.RatingBar;
import rx.Observable;
import rx.functions.Action1;

public final class RxRatingBar {
  /**
   * Create an observable of the rating changes on {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  public static Observable<Float> ratingChanges(RatingBar view) {
    return Observable.create(new RatingBarRatingChangeOnSubscribe(view));
  }

  /**
   * Create an observable of the rating change events on {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  public static Observable<RatingBarChangeEvent> ratingChangeEvents(RatingBar view) {
    return Observable.create(new RatingBarRatingChangeEventOnSubscribe(view));
  }

  /**
   * An action which sets the rating of {@code view}.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
   * to free this reference.
   */
  public static Action1<? super Float> setRating(final RatingBar view) {
    return new Action1<Float>() {
      @Override public void call(Float value) {
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
  public static Action1<? super Boolean> setIsIndicator(final RatingBar view) {
    return new Action1<Boolean>() {
      @Override public void call(Boolean value) {
        view.setIsIndicator(value);
      }
    };
  }
}
