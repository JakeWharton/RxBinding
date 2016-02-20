package com.jakewharton.rxbinding.view;

import android.support.annotation.NonNull;
import android.view.View;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;
import static com.jakewharton.rxbinding.view.ViewAttachEvent.Kind.ATTACH;
import static com.jakewharton.rxbinding.view.ViewAttachEvent.Kind.DETACH;

final class ViewAttachEventOnSubscribe implements Observable.OnSubscribe<ViewAttachEvent> {
  final View view;

  ViewAttachEventOnSubscribe(View view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super ViewAttachEvent> subscriber) {
    verifyMainThread();

    final View.OnAttachStateChangeListener listener = new View.OnAttachStateChangeListener() {
      @Override public void onViewAttachedToWindow(@NonNull final View v) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(ViewAttachEvent.create(view, ATTACH));
        }
      }

      @Override public void onViewDetachedFromWindow(@NonNull final View v) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(ViewAttachEvent.create(view, DETACH));
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
