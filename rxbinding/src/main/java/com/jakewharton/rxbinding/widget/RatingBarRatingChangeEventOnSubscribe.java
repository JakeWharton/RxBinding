package com.jakewharton.rxbinding.widget;

import android.widget.RatingBar;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class RatingBarRatingChangeEventOnSubscribe
    implements Observable.OnSubscribe<RatingBarChangeEvent> {
  final RatingBar view;

  public RatingBarRatingChangeEventOnSubscribe(RatingBar view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super RatingBarChangeEvent> subscriber) {
    verifyMainThread();

    RatingBar.OnRatingBarChangeListener listener = new RatingBar.OnRatingBarChangeListener() {
      @Override public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(RatingBarChangeEvent.create(ratingBar, rating, fromUser));
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
    subscriber.onNext(RatingBarChangeEvent.create(view, view.getRating(), false));
  }
}
