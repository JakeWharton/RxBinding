package com.jakewharton.rxbinding.support.v7.widget;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;

import com.jakewharton.rxbinding.internal.MainThreadSubscription;

import rx.Observable;
import rx.Subscriber;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class RecyclerAdapterDataChangeOnSubscribe<T extends RecyclerView.Adapter<? extends RecyclerView.ViewHolder>>
        implements Observable.OnSubscribe<T> {
  private final T adapter;

  public RecyclerAdapterDataChangeOnSubscribe(T adapter) {
    this.adapter = adapter;
  }

  @Override
  public void call(final Subscriber<? super T> subscriber) {
    checkUiThread();
    final AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
      @Override
      public void onChanged() {
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
