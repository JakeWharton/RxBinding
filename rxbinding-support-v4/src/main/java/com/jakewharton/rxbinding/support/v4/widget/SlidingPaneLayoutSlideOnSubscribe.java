package com.jakewharton.rxbinding.support.v4.widget;

import android.support.v4.widget.SlidingPaneLayout;
import android.view.View;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class SlidingPaneLayoutSlideOnSubscribe implements Observable.OnSubscribe<Float> {
  final SlidingPaneLayout view;

  SlidingPaneLayoutSlideOnSubscribe(SlidingPaneLayout view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Float> subscriber) {
    verifyMainThread();

    final SlidingPaneLayout.PanelSlideListener listener =
        new SlidingPaneLayout.SimplePanelSlideListener() {
          @Override public void onPanelSlide(View panel, float slideOffset) {
            if (!subscriber.isUnsubscribed()) {
              subscriber.onNext(slideOffset);
            }
          }
        };

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setPanelSlideListener(null);
      }
    });

    view.setPanelSlideListener(listener);
  }
}
