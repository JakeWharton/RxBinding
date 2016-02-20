package com.jakewharton.rxbinding.support.v7.widget;

import android.support.v7.widget.ActionMenuView;
import android.view.MenuItem;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class ActionMenuViewItemClickOnSubscribe implements Observable.OnSubscribe<MenuItem> {
  final ActionMenuView view;

  ActionMenuViewItemClickOnSubscribe(ActionMenuView view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super MenuItem> subscriber) {
    verifyMainThread();

    ActionMenuView.OnMenuItemClickListener listener =
      new ActionMenuView.OnMenuItemClickListener() {
        @Override public boolean onMenuItemClick(MenuItem item) {
          if (!subscriber.isUnsubscribed()) {
            subscriber.onNext(item);
          }
          return true;
        }
      };
    view.setOnMenuItemClickListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override
      protected void onUnsubscribe() {
        view.setOnMenuItemClickListener(null);
      }
    });
  }
}
