package com.jakewharton.rxbinding.view;

import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class ViewTreeObserverGlobalLayoutOnSubscribe implements Observable.OnSubscribe<Void> {
  final View view;

  ViewTreeObserverGlobalLayoutOnSubscribe(View view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Void> subscriber) {
    verifyMainThread();

    final OnGlobalLayoutListener listener = new OnGlobalLayoutListener() {
      @Override public void onGlobalLayout() {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(null);
        }
      }
    };

    view.getViewTreeObserver().addOnGlobalLayoutListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
          view.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        } else {
          view.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        }
      }
    });
  }
}
