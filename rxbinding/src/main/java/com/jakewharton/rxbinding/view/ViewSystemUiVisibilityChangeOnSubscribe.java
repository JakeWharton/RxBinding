package com.jakewharton.rxbinding.view;

import android.view.View;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class ViewSystemUiVisibilityChangeOnSubscribe implements Observable.OnSubscribe<Integer> {
  final View view;

  ViewSystemUiVisibilityChangeOnSubscribe(View view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Integer> subscriber) {
    checkUiThread();

    final View.OnSystemUiVisibilityChangeListener listener =
        new View.OnSystemUiVisibilityChangeListener() {
          @Override public void onSystemUiVisibilityChange(int visibility) {
            if (!subscriber.isUnsubscribed()) {
              subscriber.onNext(visibility);
            }
          }
        };
    view.setOnSystemUiVisibilityChangeListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnSystemUiVisibilityChangeListener(null);
      }
    });
  }
}
