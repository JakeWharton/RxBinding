package com.jakewharton.rxbinding.widget;

import android.view.View;
import android.widget.AdapterView;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import com.jakewharton.rxbinding.internal.AndroidSubscriptions;
import rx.functions.Action0;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class AdapterViewItemClickOnSubscribe implements Observable.OnSubscribe<Integer> {
  private final AdapterView<?> view;

  public AdapterViewItemClickOnSubscribe(AdapterView<?> view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Integer> subscriber) {
    checkUiThread();

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(position);
        }
      }
    };

    Subscription subscription = AndroidSubscriptions.unsubscribeOnMainThread(new Action0() {
      @Override public void call() {
        view.setOnItemClickListener(null);
      }
    });
    subscriber.add(subscription);

    view.setOnItemClickListener(listener);
  }
}
