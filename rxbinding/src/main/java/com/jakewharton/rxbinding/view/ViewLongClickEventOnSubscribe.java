package com.jakewharton.rxbinding.view;

import android.view.View;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class ViewLongClickEventOnSubscribe implements Observable.OnSubscribe<ViewLongClickEvent> {
  private final View view;
  private final Func1<? super ViewLongClickEvent, Boolean> handled;

  ViewLongClickEventOnSubscribe(View view, Func1<? super ViewLongClickEvent, Boolean> handled) {
    this.view = view;
    this.handled = handled;
  }

  @Override public void call(final Subscriber<? super ViewLongClickEvent> subscriber) {
    checkUiThread();

    View.OnLongClickListener listener = new View.OnLongClickListener() {
      @Override public boolean onLongClick(View v) {
        ViewLongClickEvent event = ViewLongClickEvent.create(view);
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
        view.setOnLongClickListener(null);
      }
    });

    view.setOnLongClickListener(listener);
  }
}
