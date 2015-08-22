package com.jakewharton.rxbinding.support.design.widget;

import android.support.design.widget.Snackbar;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import rx.Observable;
import rx.Subscriber;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class SnackbarDismissesOnSubscribe implements Observable.OnSubscribe<Integer> {
  private final Snackbar view;

  public SnackbarDismissesOnSubscribe(Snackbar view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Integer> subscriber) {
    checkUiThread();

    Snackbar.Callback callback = new Snackbar.Callback() {
      @Override public void onDismissed(Snackbar snackbar, int event) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(event);
        }
      }
    };

    view.setCallback(callback);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setCallback(null);
      }
    });
  }
}
