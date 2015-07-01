package com.jakewharton.rxbinding.view;

import android.view.View;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import rx.Observable;
import rx.Subscriber;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class ViewFocusChangeEventOnSubscribe
    implements Observable.OnSubscribe<ViewFocusChangeEvent> {
  private final View view;

  public ViewFocusChangeEventOnSubscribe(View view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super ViewFocusChangeEvent> subscriber) {
    checkUiThread();

    View.OnFocusChangeListener listener = new View.OnFocusChangeListener() {
      @Override public void onFocusChange(View v, boolean hasFocus) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(ViewFocusChangeEvent.create(view, hasFocus));
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
    subscriber.onNext(ViewFocusChangeEvent.create(view, view.hasFocus()));
  }
}
