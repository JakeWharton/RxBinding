package com.jakewharton.rxbinding.view;

import android.view.View;
import android.view.ViewTreeObserver;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;
import rx.functions.Func0;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class ViewTreeObserverPreDrawOnSubscribe implements Observable.OnSubscribe<Void> {
  final View view;
  final Func0<Boolean> proceedDrawingPass;

  ViewTreeObserverPreDrawOnSubscribe(View view, Func0<Boolean> proceedDrawingPass) {
    this.view = view;
    this.proceedDrawingPass = proceedDrawingPass;
  }

  @Override public void call(final Subscriber<? super Void> subscriber) {
    verifyMainThread();

    final ViewTreeObserver.OnPreDrawListener listener = new ViewTreeObserver.OnPreDrawListener() {
      @Override public boolean onPreDraw() {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(null);
          return proceedDrawingPass.call();
        }
        return true;
      }
    };

    view.getViewTreeObserver().addOnPreDrawListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.getViewTreeObserver().removeOnPreDrawListener(listener);
      }
    });
  }
}
