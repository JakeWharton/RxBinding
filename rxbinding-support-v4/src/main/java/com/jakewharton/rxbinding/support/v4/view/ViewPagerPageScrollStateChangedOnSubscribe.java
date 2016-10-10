package com.jakewharton.rxbinding.support.v4.view;

import android.support.v4.view.ViewPager;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class ViewPagerPageScrollStateChangedOnSubscribe implements Observable.OnSubscribe<Integer> {
  final ViewPager view;

  ViewPagerPageScrollStateChangedOnSubscribe(ViewPager view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Integer> subscriber) {
    verifyMainThread();

    final ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
      }

      @Override public void onPageSelected(int position) {
      }

      @Override public void onPageScrollStateChanged(int state) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(state);
        }
      }
    };

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.removeOnPageChangeListener(listener);
      }
    });

    view.addOnPageChangeListener(listener);
  }
}
