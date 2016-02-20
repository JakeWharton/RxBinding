package com.jakewharton.rxbinding.widget;

import android.view.View;
import android.widget.AdapterView;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class AdapterViewItemClickOnSubscribe implements Observable.OnSubscribe<Integer> {
  final AdapterView<?> view;

  public AdapterViewItemClickOnSubscribe(AdapterView<?> view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Integer> subscriber) {
    verifyMainThread();

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(position);
        }
      }
    };
    view.setOnItemClickListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnItemClickListener(null);
      }
    });
  }
}
