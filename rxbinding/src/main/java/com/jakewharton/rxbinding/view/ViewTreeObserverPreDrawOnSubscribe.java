package com.jakewharton.rxbinding.view;

import android.view.View;
import android.view.ViewTreeObserver;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class ViewTreeObserverPreDrawOnSubscribe
    implements Observable.OnSubscribe<Object> {
  private final Object event = new Object();
  private final View view;
  private final Func1<? super Object, Boolean> proceedDrawingPass;

  public ViewTreeObserverPreDrawOnSubscribe(View view,
      Func1<? super Object, Boolean> proceedDrawingPass) {
    this.view = view;
    this.proceedDrawingPass = proceedDrawingPass;
  }

  @Override public void call(final Subscriber<? super Object> subscriber) {
    checkUiThread();

    final ViewTreeObserver.OnPreDrawListener listener = new ViewTreeObserver.OnPreDrawListener() {
      @Override public boolean onPreDraw() {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(event);
          return proceedDrawingPass.call(event);
        }
        return true;
      }
    };

    view.getViewTreeObserver().addOnPreDrawListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.getViewTreeObserver().removeOnPreDrawListener(listener);
      }
    });
  }
}
