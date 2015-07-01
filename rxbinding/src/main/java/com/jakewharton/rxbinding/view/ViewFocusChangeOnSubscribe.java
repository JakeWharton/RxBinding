package com.jakewharton.rxbinding.view;

import android.view.View;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import rx.Observable;
import rx.Subscriber;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class ViewFocusChangeOnSubscribe implements Observable.OnSubscribe<Boolean> {
  private final View view;

  public ViewFocusChangeOnSubscribe(View view) {
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

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnFocusChangeListener(null);
      }
    });

    view.setOnFocusChangeListener(listener);

    // Send out the initial value.
    subscriber.onNext(view.hasFocus());
  }
}
