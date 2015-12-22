package com.jakewharton.rxbinding.support.v4.view;

import android.support.v4.view.ViewPager;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class ViewPagerPageSelectedOnSubscribe implements Observable.OnSubscribe<Integer> {
  final ViewPager view;

  ViewPagerPageSelectedOnSubscribe(ViewPager view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Integer> subscriber) {
    checkUiThread();

    final ViewPager.OnPageChangeListener listener = new ViewPager.SimpleOnPageChangeListener() {
      @Override public void onPageSelected(int position) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(position);
        }
      }
    };
    view.addOnPageChangeListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.removeOnPageChangeListener(listener);
      }
    });

    // Emit initial value.
    subscriber.onNext(view.getCurrentItem());
  }
}
