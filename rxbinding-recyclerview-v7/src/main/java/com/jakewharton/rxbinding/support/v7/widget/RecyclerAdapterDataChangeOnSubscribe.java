package com.jakewharton.rxbinding.support.v7.widget;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.support.v7.widget.RecyclerView.ViewHolder;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class RecyclerAdapterDataChangeOnSubscribe<T extends Adapter<? extends ViewHolder>>
    implements Observable.OnSubscribe<T> {
  final T adapter;

  RecyclerAdapterDataChangeOnSubscribe(T adapter) {
    this.adapter = adapter;
  }

  @Override public void call(final Subscriber<? super T> subscriber) {
    verifyMainThread();

    final AdapterDataObserver observer = new AdapterDataObserver() {
      @Override public void onChanged() {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(adapter);
        }
      }
    };

    adapter.registerAdapterDataObserver(observer);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        adapter.unregisterAdapterDataObserver(observer);
      }
    });

    // Emit initial value.
    subscriber.onNext(adapter);
  }
}
