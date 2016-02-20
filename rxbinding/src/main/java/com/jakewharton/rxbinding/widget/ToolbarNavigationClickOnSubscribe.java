package com.jakewharton.rxbinding.widget;

import android.annotation.TargetApi;
import android.view.View;
import android.widget.Toolbar;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static rx.android.MainThreadSubscription.verifyMainThread;

@TargetApi(LOLLIPOP)
final class ToolbarNavigationClickOnSubscribe implements Observable.OnSubscribe<Void> {
  final Toolbar view;

  public ToolbarNavigationClickOnSubscribe(Toolbar view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Void> subscriber) {
    verifyMainThread();

    View.OnClickListener listener = new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(null);
        }
      }
    };
    view.setNavigationOnClickListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setNavigationOnClickListener(null);
      }
    });
  }
}
