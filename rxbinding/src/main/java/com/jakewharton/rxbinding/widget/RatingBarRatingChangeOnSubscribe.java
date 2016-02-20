package com.jakewharton.rxbinding.widget;

import android.widget.RatingBar;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class RatingBarRatingChangeOnSubscribe implements Observable.OnSubscribe<Float> {
  final RatingBar view;

  public RatingBarRatingChangeOnSubscribe(RatingBar view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Float> subscriber) {
    verifyMainThread();

    RatingBar.OnRatingBarChangeListener listener = new RatingBar.OnRatingBarChangeListener() {
      @Override public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(rating);
        }
      }
    };
    view.setOnRatingBarChangeListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnRatingBarChangeListener(null);
      }
    });

    // Emit initial value.
    subscriber.onNext(view.getRating());
  }
}
