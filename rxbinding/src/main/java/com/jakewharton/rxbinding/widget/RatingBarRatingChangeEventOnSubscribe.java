package com.jakewharton.rxbinding.widget;

import android.widget.RatingBar;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class RatingBarRatingChangeEventOnSubscribe
    implements Observable.OnSubscribe<RatingBarChangeEvent> {
  private final RatingBar view;

  public RatingBarRatingChangeEventOnSubscribe(RatingBar view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super RatingBarChangeEvent> subscriber) {
    checkUiThread();

    RatingBar.OnRatingBarChangeListener listener = new RatingBar.OnRatingBarChangeListener() {
      @Override public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        subscriber.onNext(RatingBarChangeEvent.create(ratingBar, rating, fromUser));
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
