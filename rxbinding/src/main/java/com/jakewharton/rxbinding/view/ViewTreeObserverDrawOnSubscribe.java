package com.jakewharton.rxbinding.view;

import android.annotation.TargetApi;
import android.view.View;
import android.view.ViewTreeObserver;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import rx.Observable;
import rx.Subscriber;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;
import static android.os.Build.VERSION_CODES.JELLY_BEAN;

@TargetApi(JELLY_BEAN)
final class ViewTreeObserverDrawOnSubscribe
    implements Observable.OnSubscribe<Object> {
  private final Object event = new Object();
  private final View view;

  public ViewTreeObserverDrawOnSubscribe(View view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Object> subscriber) {
    checkUiThread();

    final ViewTreeObserver.OnDrawListener listener = new ViewTreeObserver.OnDrawListener() {
      @Override public void onDraw() {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(event);
        }
      }
    };

    view.getViewTreeObserver().addOnDrawListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.getViewTreeObserver().removeOnDrawListener(listener);
      }
    });
  }
}
