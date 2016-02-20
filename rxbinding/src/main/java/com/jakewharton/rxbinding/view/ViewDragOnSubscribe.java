package com.jakewharton.rxbinding.view;

import android.view.DragEvent;
import android.view.View;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;
import rx.functions.Func1;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class ViewDragOnSubscribe implements Observable.OnSubscribe<DragEvent> {
  final View view;
  final Func1<? super DragEvent, Boolean> handled;

  ViewDragOnSubscribe(View view, Func1<? super DragEvent, Boolean> handled) {
    this.view = view;
    this.handled = handled;
  }

  @Override public void call(final Subscriber<? super DragEvent> subscriber) {
    verifyMainThread();

    View.OnDragListener listener = new View.OnDragListener() {
      @Override public boolean onDrag(View v, DragEvent event) {
        if (handled.call(event)) {
          if (!subscriber.isUnsubscribed()) {
            subscriber.onNext(event);
          }
          return true;
        }
        return false;
      }
    };
    view.setOnDragListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnDragListener(null);
      }
    });
  }
}
