package com.jakewharton.rxbinding.support.design.widget;

import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class TabLayoutSelectionsOnSubscribe implements Observable.OnSubscribe<Tab> {
  private final TabLayout view;

  public TabLayoutSelectionsOnSubscribe(TabLayout view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Tab> subscriber) {
    checkUiThread();

    final TabLayout.OnTabSelectedListener listener = new TabLayout.OnTabSelectedListener() {
      @Override public void onTabSelected(Tab tab) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(tab);
        }
      }

      @Override public void onTabUnselected(Tab tab) {
      }

      @Override public void onTabReselected(Tab tab) {
      }
    };
    view.setOnTabSelectedListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnTabSelectedListener(null);
      }
    });

    int index = view.getSelectedTabPosition();
    if (index != -1) {
      subscriber.onNext(view.getTabAt(index));
    }
  }
}
