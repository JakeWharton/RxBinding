package com.jakewharton.rxbinding.view;

import android.view.DragEvent;
import android.view.View;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class ViewDragEventOnSubscribe implements Observable.OnSubscribe<ViewDragEvent> {
  private final View view;
  private final Func1<? super ViewDragEvent, Boolean> handled;

  public ViewDragEventOnSubscribe(View view, Func1<? super ViewDragEvent, Boolean> handled) {
    this.view = view;
    this.handled = handled;
  }

  @Override public void call(final Subscriber<? super ViewDragEvent> subscriber) {
    checkUiThread();

    View.OnDragListener listener = new View.OnDragListener() {
      @Override public boolean onDrag(View v, DragEvent dragEvent) {
        ViewDragEvent event = ViewDragEvent.create(view, dragEvent);
        if (handled.call(event)) {
          if (!subscriber.isUnsubscribed()) {
            subscriber.onNext(event);
          }
          return true;
        }
        return false;
      }
    };

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnDragListener(null);
      }
    });

    view.setOnDragListener(listener);
  }
}
