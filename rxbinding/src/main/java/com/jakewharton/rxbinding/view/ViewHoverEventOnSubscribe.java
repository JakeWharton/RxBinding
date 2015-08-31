package com.jakewharton.rxbinding.view;

import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class ViewHoverEventOnSubscribe implements Observable.OnSubscribe<ViewHoverEvent> {
  private final View view;
  private final Func1<? super ViewHoverEvent, Boolean> handled;

  public ViewHoverEventOnSubscribe(View view, Func1<? super ViewHoverEvent, Boolean> handled) {
    this.view = view;
    this.handled = handled;
  }

  @Override public void call(final Subscriber<? super ViewHoverEvent> subscriber) {
    checkUiThread();

    View.OnHoverListener listener = new View.OnHoverListener() {
      @Override public boolean onHover(View v, @NonNull MotionEvent motionEvent) {
        ViewHoverEvent event = ViewHoverEvent.create(view, motionEvent);
        if (handled.call(event)) {
          if (!subscriber.isUnsubscribed()) {
            subscriber.onNext(event);
          }
          return true;
        }
        return false;
      }
    };
    view.setOnHoverListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnHoverListener(null);
      }
    });
  }
}
