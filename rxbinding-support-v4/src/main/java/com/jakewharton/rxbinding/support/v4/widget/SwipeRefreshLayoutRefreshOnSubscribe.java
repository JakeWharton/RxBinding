package com.jakewharton.rxbinding.support.v4.widget;

import android.support.v4.widget.SwipeRefreshLayout;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class SwipeRefreshLayoutRefreshOnSubscribe implements Observable.OnSubscribe<Void> {
  final SwipeRefreshLayout view;

  SwipeRefreshLayoutRefreshOnSubscribe(SwipeRefreshLayout view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Void> subscriber) {
    verifyMainThread();

    SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        subscriber.onNext(null);
      }
    };
    view.setOnRefreshListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnRefreshListener(null);
      }
    });
  }
}
