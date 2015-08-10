package com.jakewharton.rxbinding.view;

import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class ViewTouchOnSubscribe implements Observable.OnSubscribe<MotionEvent> {
  private final View view;
  private final Func1<? super MotionEvent, Boolean> handled;

  public ViewTouchOnSubscribe(View view, Func1<? super MotionEvent, Boolean> handled) {
    this.view = view;
    this.handled = handled;
  }

  @Override public void call(final Subscriber<? super MotionEvent> subscriber) {
    checkUiThread();

    View.OnTouchListener listener = new View.OnTouchListener() {
      @Override public boolean onTouch(View v, @NonNull MotionEvent event) {
        if (handled.call(event)) {
          if (!subscriber.isUnsubscribed()) {
            subscriber.onNext(event);
          }
          return true;
        }
        return false;
      }
    };
    view.setOnTouchListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnTouchListener(null);
      }
    });
  }
}
