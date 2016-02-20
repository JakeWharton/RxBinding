package com.jakewharton.rxbinding.view;

import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;
import rx.functions.Func1;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class ViewTouchOnSubscribe implements Observable.OnSubscribe<MotionEvent> {
  final View view;
  final Func1<? super MotionEvent, Boolean> handled;

  ViewTouchOnSubscribe(View view, Func1<? super MotionEvent, Boolean> handled) {
    this.view = view;
    this.handled = handled;
  }

  @Override public void call(final Subscriber<? super MotionEvent> subscriber) {
    verifyMainThread();

    View.OnTouchListener listener = new View.OnTouchListener() {
      @Override public boolean onTouch(View v, @NonNull MotionEvent event) {
        if (handled.call(event)) {
          if (!subscriber.isUnsubscribed()) {
            subscriber.onNext(event);
          }
          return true;
        }
        return false;
      }
    };
    view.setOnTouchListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnTouchListener(null);
      }
    });
  }
}
