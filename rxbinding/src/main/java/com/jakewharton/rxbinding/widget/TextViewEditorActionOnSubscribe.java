package com.jakewharton.rxbinding.widget;

import android.view.KeyEvent;
import android.widget.TextView;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;
import rx.functions.Func1;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class TextViewEditorActionOnSubscribe implements Observable.OnSubscribe<Integer> {
  final TextView view;
  final Func1<? super Integer, Boolean> handled;

  TextViewEditorActionOnSubscribe(TextView view, Func1<? super Integer, Boolean> handled) {
    this.view = view;
    this.handled = handled;
  }

  @Override public void call(final Subscriber<? super Integer> subscriber) {
    verifyMainThread();

    TextView.OnEditorActionListener listener = new TextView.OnEditorActionListener() {
      @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (handled.call(actionId)) {
          if (!subscriber.isUnsubscribed()) {
            subscriber.onNext(actionId);
          }
          return true;
        }
        return false;
      }
    };
    view.setOnEditorActionListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnEditorActionListener(null);
      }
    });
  }
}
