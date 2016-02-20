package com.jakewharton.rxbinding.support.v7.widget;

import android.support.v7.widget.RecyclerView;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class RecyclerViewScrollStateChangeOnSubscribe implements Observable.OnSubscribe<Integer> {
  final RecyclerView recyclerView;

  public RecyclerViewScrollStateChangeOnSubscribe(RecyclerView recyclerView) {
    this.recyclerView = recyclerView;
  }

  @Override public void call(final Subscriber<? super Integer> subscriber) {
    verifyMainThread();

    final RecyclerView.OnScrollListener listener = new RecyclerView.OnScrollListener() {
      @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(newState);
        }
      }
    };
    recyclerView.addOnScrollListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        recyclerView.removeOnScrollListener(listener);
      }
    });
  }
}
