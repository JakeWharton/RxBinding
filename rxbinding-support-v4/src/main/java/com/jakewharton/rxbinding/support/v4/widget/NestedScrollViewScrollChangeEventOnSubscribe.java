package com.jakewharton.rxbinding.support.v4.widget;

import android.support.v4.widget.NestedScrollView;

import android.support.v4.widget.NestedScrollView.OnScrollChangeListener;
import com.jakewharton.rxbinding.view.ViewScrollChangeEvent;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class NestedScrollViewScrollChangeEventOnSubscribe
    implements Observable.OnSubscribe<ViewScrollChangeEvent> {
  final NestedScrollView nestedScrollView;

  NestedScrollViewScrollChangeEventOnSubscribe(NestedScrollView view) {
    this.nestedScrollView = view;
  }

  @Override public void call(final Subscriber<? super ViewScrollChangeEvent> subscriber) {
    verifyMainThread();

    final OnScrollChangeListener listener = new OnScrollChangeListener() {
      @Override
      public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX,
          int oldScrollY) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(
              ViewScrollChangeEvent.create(nestedScrollView, scrollX, scrollY, oldScrollX,
                  oldScrollY));
        }
      }
    };

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        nestedScrollView.setOnScrollChangeListener((OnScrollChangeListener) null);
      }
    });

    nestedScrollView.setOnScrollChangeListener(listener);
  }
}

