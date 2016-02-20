package com.jakewharton.rxbinding.support.v7.widget;

import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class PopupMenuItemClickOnSubscribe implements Observable.OnSubscribe<MenuItem> {

  final PopupMenu view;

  public PopupMenuItemClickOnSubscribe(PopupMenu view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super MenuItem> subscriber) {
    verifyMainThread();

    PopupMenu.OnMenuItemClickListener listener = new PopupMenu.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(item);
        }
        return true;
      }
    };

    view.setOnMenuItemClickListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnMenuItemClickListener(null);
      }
    });
  }
}
