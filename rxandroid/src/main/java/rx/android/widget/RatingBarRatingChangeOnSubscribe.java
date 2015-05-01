package rx.android.widget;

import android.widget.RatingBar;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

import static rx.android.internal.Preconditions.checkUiThread;

final class RatingBarRatingChangeOnSubscribe implements Observable.OnSubscribe<Float> {
  private final RatingBar view;

  public RatingBarRatingChangeOnSubscribe(RatingBar view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Float> subscriber) {
    checkUiThread();

    RatingBar.OnRatingBarChangeListener listener = new RatingBar.OnRatingBarChangeListener() {
      @Override public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        subscriber.onNext(rating);
      }
    };

    Subscription subscription = Subscriptions.create(new Action0() {
      @Override public void call() {
        view.setOnRatingBarChangeListener(null);
      }
    });
    subscriber.add(subscription);

    view.setOnRatingBarChangeListener(listener);
  }
}
