package com.jakewharton.rxbinding.support.design.widget;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.View.OnClickListener;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

final class SnackbarActionOnSubscribe implements Observable.OnSubscribe<Integer> {
  final Snackbar view;
  final int resId;
  final CharSequence text;

  public SnackbarActionOnSubscribe(Snackbar view, int resId) {
      this.view = view;
      this.text = null;
      this.resId = resId;
  }
  public SnackbarActionOnSubscribe(Snackbar view, CharSequence text) {
      this.view = view;
      this.text = text;
      this.resId = -1;
  }

  @Override
  public void call(final Subscriber<? super Integer> subscriber) {
    MainThreadSubscription.verifyMainThread();

    View.OnClickListener listener = new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(null);
        }
      }
    };

    subscriber.add(new MainThreadSubscription() {
      @Override
      protected void onUnsubscribe() {
        view.setCallback(null);
      }
    });

    if (text == null) {
        view.setAction(resId, listener);
    } else {
        view.setAction(text, listener);
    }
  }
}