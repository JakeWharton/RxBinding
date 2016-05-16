package com.jakewharton.rxbinding.support.v4.widget;

import android.support.v4.widget.NestedScrollView;

import com.jakewharton.rxbinding.view.ViewScrollChangeEvent;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class NestedScrollViewScrollChangeEventOnSubscribe
    implements Observable.OnSubscribe<ViewScrollChangeEvent> {
  final NestedScrollView nestedScrollView;

  NestedScrollViewScrollChangeEventOnSubscribe(NestedScrollView nestedScrollView) {
    this.nestedScrollView = nestedScrollView;
  }

  @Override public void call(final Subscriber<? super ViewScrollChangeEvent> subscriber) {
    verifyMainThread();

    final NestedScrollView.OnScrollChangeListener listener =
        new NestedScrollView.OnScrollChangeListener() {
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
    nestedScrollView.setOnScrollChangeListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Overrideł protected void onUnsubscribe() {
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) null);
      }
    });
  }
}

