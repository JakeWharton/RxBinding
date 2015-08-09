package com.jakewharton.rxbinding.view;

import android.support.annotation.NonNull;
import android.view.View;

import com.jakewharton.rxbinding.internal.MainThreadSubscription;

import rx.Observable;
import rx.Subscriber;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;
import static com.jakewharton.rxbinding.view.ViewAttachEvent.ATTACH;
import static com.jakewharton.rxbinding.view.ViewAttachEvent.DETACH;

final class ViewAttachEventOnSubscribe implements Observable.OnSubscribe<ViewAttachEvent> {
  private final View view;

  ViewAttachEventOnSubscribe(View view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super ViewAttachEvent> subscriber) {
    checkUiThread();

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

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.removeOnAttachStateChangeListener(listener);
      }
    });

    view.addOnAttachStateChangeListener(listener);
  }
}
