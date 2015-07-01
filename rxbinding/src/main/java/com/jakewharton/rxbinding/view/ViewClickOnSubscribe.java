package com.jakewharton.rxbinding.view;

import android.view.View;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import com.jakewharton.rxbinding.internal.AndroidSubscriptions;
import rx.functions.Action0;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class ViewClickOnSubscribe implements Observable.OnSubscribe<Object> {
  private final Object event = new Object();
  private final View view;

  ViewClickOnSubscribe(View view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Object> subscriber) {
    checkUiThread();

    View.OnClickListener listener = new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(event);
        }
      }
    };

    Subscription subscription = AndroidSubscriptions.unsubscribeOnMainThread(new Action0() {
      @Override public void call() {
        view.setOnClickListener(null);
      }
    });
    subscriber.add(subscription);

    view.setOnClickListener(listener);
  }
}
