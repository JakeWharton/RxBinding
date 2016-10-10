package com.jakewharton.rxbinding.support.v4.widget;

import android.support.v4.widget.SlidingPaneLayout;
import android.view.View;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class SlidingPaneLayoutPaneOpenedOnSubscribe implements Observable.OnSubscribe<Boolean> {
  final SlidingPaneLayout view;

  SlidingPaneLayoutPaneOpenedOnSubscribe(SlidingPaneLayout view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Boolean> subscriber) {
    verifyMainThread();

    final SlidingPaneLayout.PanelSlideListener listener =
        new SlidingPaneLayout.SimplePanelSlideListener() {
          @Override public void onPanelOpened(View panel) {
            if (!subscriber.isUnsubscribed()) {
              subscriber.onNext(true);
            }
          }

          @Override public void onPanelClosed(View panel) {
            if (!subscriber.isUnsubscribed()) {
              subscriber.onNext(false);
            }
          }
        };

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setPanelSlideListener(null);
      }
    });

    view.setPanelSlideListener(listener);

    subscriber.onNext(view.isOpen());
  }
}
