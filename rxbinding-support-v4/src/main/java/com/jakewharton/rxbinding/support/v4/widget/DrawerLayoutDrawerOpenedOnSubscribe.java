package com.jakewharton.rxbinding.support.v4.widget;

import android.support.v4.widget.DrawerLayout;
import android.view.View;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import rx.Observable;
import rx.Subscriber;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class DrawerLayoutDrawerOpenedOnSubscribe implements Observable.OnSubscribe<Boolean> {
  private DrawerLayout view;

  DrawerLayoutDrawerOpenedOnSubscribe(DrawerLayout view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Boolean> subscriber) {
    checkUiThread();

    DrawerLayout.DrawerListener listener = new DrawerLayout.SimpleDrawerListener() {
      @Override public void onDrawerOpened(View drawerView) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(true);
        }
      }

      @Override public void onDrawerSlide(View drawerView, float slideOffset) {
      }

      @Override public void onDrawerStateChanged(int newState) {
      }

      @Override public void onDrawerClosed(View drawerView) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(false);
        }
      }
    };

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setDrawerListener(null);
      }
    });

    view.setDrawerListener(listener);
  }
}
