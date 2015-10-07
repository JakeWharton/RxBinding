package com.jakewharton.rxbinding.support.v7.widget;

import android.support.v7.app.ActionBar;
import android.view.View;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import rx.Observable;
import rx.Subscriber;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class ActionBarMenuVisibilityChangeOnSubscribe implements Observable.OnSubscribe<Boolean> {
  private final ActionBar view;

  ActionBarMenuVisibilityChangeOnSubscribe(ActionBar view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Boolean> subscriber) {
    checkUiThread();

    final ActionBar.OnMenuVisibilityListener listener = new ActionBar.OnMenuVisibilityListener() {
      @Override public void onMenuVisibilityChanged(boolean isVisible) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(isVisible);
        }
      }
    };
    view.addOnMenuVisibilityListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.removeOnMenuVisibilityListener(listener);
      }
    });
  }
}
