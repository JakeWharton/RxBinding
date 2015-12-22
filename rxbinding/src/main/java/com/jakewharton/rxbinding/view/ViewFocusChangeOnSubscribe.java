package com.jakewharton.rxbinding.view;

import android.view.View;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class ViewFocusChangeOnSubscribe implements Observable.OnSubscribe<Boolean> {
  final View view;

  ViewFocusChangeOnSubscribe(View view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Boolean> subscriber) {
    checkUiThread();

    View.OnFocusChangeListener listener = new View.OnFocusChangeListener() {
      @Override public void onFocusChange(View v, boolean hasFocus) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(hasFocus);
        }
      }
    };
    view.setOnFocusChangeListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnFocusChangeListener(null);
      }
    });

    // Emit initial value.
    subscriber.onNext(view.hasFocus());
  }
}
