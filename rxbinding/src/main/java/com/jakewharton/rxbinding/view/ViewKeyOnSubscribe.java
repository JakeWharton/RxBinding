package com.jakewharton.rxbinding.view;

import android.view.KeyEvent;
import android.view.View;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;
import rx.functions.Func1;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class ViewKeyOnSubscribe implements Observable.OnSubscribe<KeyEvent> {
  private final View view;
  private final Func1<? super KeyEvent, Boolean> handled;

  ViewKeyOnSubscribe(View view, Func1<? super KeyEvent, Boolean> handled) {
    this.view = view;
    this.handled = handled;
  }

  @Override public void call(final Subscriber<? super KeyEvent> subscriber) {
    verifyMainThread();

    View.OnKeyListener keyListener = new View.OnKeyListener() {
      @Override public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if (subscriber.isUnsubscribed()) {
          return false;
        }

        if (!handled.call(keyEvent)) {
          return false;
        } else {
          subscriber.onNext(keyEvent);
          return true;
        }
      }
    };

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnKeyListener(null);
      }
    });

    view.setOnKeyListener(keyListener);
  }
}
