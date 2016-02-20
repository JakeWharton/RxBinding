package com.jakewharton.rxbinding.widget;

import android.annotation.TargetApi;
import android.view.MenuItem;
import android.widget.Toolbar;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static rx.android.MainThreadSubscription.verifyMainThread;

@TargetApi(LOLLIPOP)
final class ToolbarItemClickOnSubscribe implements Observable.OnSubscribe<MenuItem> {
  final Toolbar view;

  ToolbarItemClickOnSubscribe(Toolbar view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super MenuItem> subscriber) {
    verifyMainThread();

    Toolbar.OnMenuItemClickListener listener = new Toolbar.OnMenuItemClickListener() {
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
