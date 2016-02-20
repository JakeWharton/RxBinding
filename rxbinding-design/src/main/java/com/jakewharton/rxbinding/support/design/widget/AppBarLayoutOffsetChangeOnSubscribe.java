package com.jakewharton.rxbinding.support.design.widget;

import android.support.design.widget.AppBarLayout;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class AppBarLayoutOffsetChangeOnSubscribe implements Observable.OnSubscribe<Integer> {
  final AppBarLayout view;

  AppBarLayoutOffsetChangeOnSubscribe(AppBarLayout view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Integer> subscriber) {
    verifyMainThread();

    final AppBarLayout.OnOffsetChangedListener listener =
        new AppBarLayout.OnOffsetChangedListener() {
          @Override public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            if (!subscriber.isUnsubscribed()) {
              subscriber.onNext(verticalOffset);
            }
          }
        };
    view.addOnOffsetChangedListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.removeOnOffsetChangedListener(listener);
      }
    });
  }
}
