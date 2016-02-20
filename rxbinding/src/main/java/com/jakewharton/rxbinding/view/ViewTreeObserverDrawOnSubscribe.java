package com.jakewharton.rxbinding.view;

import android.annotation.TargetApi;
import android.view.View;
import android.view.ViewTreeObserver;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;
import static android.os.Build.VERSION_CODES.JELLY_BEAN;

@TargetApi(JELLY_BEAN)
final class ViewTreeObserverDrawOnSubscribe implements Observable.OnSubscribe<Void> {
  final View view;

  ViewTreeObserverDrawOnSubscribe(View view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Void> subscriber) {
    verifyMainThread();

    final ViewTreeObserver.OnDrawListener listener = new ViewTreeObserver.OnDrawListener() {
      @Override public void onDraw() {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(null);
        }
      }
    };

    view.getViewTreeObserver().addOnDrawListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.getViewTreeObserver().removeOnDrawListener(listener);
      }
    });
  }
}
