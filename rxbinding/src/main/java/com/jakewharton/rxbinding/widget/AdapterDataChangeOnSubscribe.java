package com.jakewharton.rxbinding.widget;

import android.database.DataSetObserver;
import android.widget.Adapter;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class AdapterDataChangeOnSubscribe<T extends Adapter>
    implements Observable.OnSubscribe<T> {
  final T adapter;

  public AdapterDataChangeOnSubscribe(T adapter) {
    this.adapter = adapter;
  }

  @Override public void call(final Subscriber<? super T> subscriber) {
    verifyMainThread();

    final DataSetObserver observer = new DataSetObserver() {
      @Override public void onChanged() {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(adapter);
        }
      }
    };

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        adapter.unregisterDataSetObserver(observer);
      }
    });

    adapter.registerDataSetObserver(observer);

    // Emit initial value.
    subscriber.onNext(adapter);
  }
}
