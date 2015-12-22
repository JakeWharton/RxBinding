package com.jakewharton.rxbinding.view;

import android.view.View;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class ViewClickOnSubscribe implements Observable.OnSubscribe<Void> {
  final View view;

  ViewClickOnSubscribe(View view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Void> subscriber) {
    checkUiThread();

    View.OnClickListener listener = new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(null);
        }
      }
    };
    view.setOnClickListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnClickListener(null);
      }
    });
  }
}
