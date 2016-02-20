package com.jakewharton.rxbinding.view;

import android.support.annotation.NonNull;
import android.view.View;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class ViewAttachesOnSubscribe implements Observable.OnSubscribe<Void> {
  final boolean callOnAttach;
  final View view;

  ViewAttachesOnSubscribe(View view, boolean callOnAttach) {
    this.view = view;
    this.callOnAttach = callOnAttach;
  }

  @Override public void call(final Subscriber<? super Void> subscriber) {
    verifyMainThread();

    final View.OnAttachStateChangeListener listener = new View.OnAttachStateChangeListener() {
      @Override public void onViewAttachedToWindow(@NonNull final View v) {
        if (callOnAttach && !subscriber.isUnsubscribed()) {
          subscriber.onNext(null);
        }
      }

      @Override public void onViewDetachedFromWindow(@NonNull final View v) {
        if (!callOnAttach && !subscriber.isUnsubscribed()) {
          subscriber.onNext(null);
        }
      }
    };
    view.addOnAttachStateChangeListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.removeOnAttachStateChangeListener(listener);
      }
    });
  }
}
