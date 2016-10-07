package com.jakewharton.rxbinding.view;

import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;
import rx.functions.Func1;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class ViewKeyOnSubscribe implements Observable.OnSubscribe<ViewKeyEvent> {
  private final View view;
  private final Func1<? super ViewKeyEvent, Boolean> handled;

  ViewKeyOnSubscribe(View view, Func1<? super ViewKeyEvent, Boolean> handled) {
    this.view = view;
    this.handled = handled;
  }

  @Override public void call(final Subscriber<? super ViewKeyEvent> subscriber) {
    verifyMainThread();

    View.OnKeyListener keyListener = new View.OnKeyListener() {
      @Override public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if (subscriber.isUnsubscribed()) {
          return false;
        }

        ViewKeyEvent event = new ViewKeyEvent(view, keyCode, keyEvent);
        if (!handled.call(event)) {
          return false;
        } else {
          subscriber.onNext(event);
          return true;
        }
      }
    };
    view.setOnKeyListener(keyListener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnKeyListener(null);
      }
    });
  }
}
