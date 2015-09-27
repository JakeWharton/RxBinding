package com.jakewharton.rxbinding.view;

import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import rx.Observable;
import rx.Subscriber;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class ViewTreeObserverGlobalLayoutOnSubscribe implements Observable.OnSubscribe<Object> {
  private final Object event = new Object();
  private final View view;

  ViewTreeObserverGlobalLayoutOnSubscribe(View view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Object> subscriber) {
    checkUiThread();

    final ViewTreeObserver.OnGlobalLayoutListener listener = new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override public void onGlobalLayout() {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(event);
        }
      }
    };

    view.getViewTreeObserver().addOnGlobalLayoutListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
          view.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        } else {
          view.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        }
      }
    });
  }
}
