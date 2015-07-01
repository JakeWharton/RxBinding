package com.jakewharton.rxbinding.view;

import android.view.View;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import rx.Observable;
import rx.Subscriber;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class ViewClickEventOnSubscribe implements Observable.OnSubscribe<ViewClickEvent> {
  private final View view;

  ViewClickEventOnSubscribe(View view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super ViewClickEvent> subscriber) {
    checkUiThread();

    View.OnClickListener listener = new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(ViewClickEvent.create(view));
        }
      }
    };

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnClickListener(null);
      }
    });

    view.setOnClickListener(listener);
  }
}
