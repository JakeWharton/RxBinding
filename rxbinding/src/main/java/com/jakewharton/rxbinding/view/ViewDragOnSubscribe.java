package com.jakewharton.rxbinding.view;

import android.view.DragEvent;
import android.view.View;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import com.jakewharton.rxbinding.internal.AndroidSubscriptions;
import rx.functions.Action0;
import rx.functions.Func1;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class ViewDragOnSubscribe implements Observable.OnSubscribe<DragEvent> {
  private final View view;
  private final Func1<? super DragEvent, Boolean> handled;

  ViewDragOnSubscribe(View view, Func1<? super DragEvent, Boolean> handled) {
    this.view = view;
    this.handled = handled;
  }

  @Override public void call(final Subscriber<? super DragEvent> subscriber) {
    checkUiThread();

    View.OnDragListener listener = new View.OnDragListener() {
      @Override public boolean onDrag(View v, DragEvent event) {
        if (handled.call(event)) {
          subscriber.onNext(event);
          return true;
        }
        return false;
      }
    };

    Subscription subscription = AndroidSubscriptions.unsubscribeOnMainThread(new Action0() {
      @Override public void call() {
        view.setOnDragListener(null);
      }
    });
    subscriber.add(subscription);

    view.setOnDragListener(listener);
  }
}
