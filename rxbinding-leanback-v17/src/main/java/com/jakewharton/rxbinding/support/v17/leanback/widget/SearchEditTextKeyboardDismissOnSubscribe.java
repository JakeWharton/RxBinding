package com.jakewharton.rxbinding.support.v17.leanback.widget;

import android.support.v17.leanback.widget.SearchEditText;
import android.support.v17.leanback.widget.SearchEditText.OnKeyboardDismissListener;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class SearchEditTextKeyboardDismissOnSubscribe implements Observable.OnSubscribe<Void> {
  final SearchEditText view;

  public SearchEditTextKeyboardDismissOnSubscribe(SearchEditText view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Void> subscriber) {
    verifyMainThread();

    OnKeyboardDismissListener listener = new OnKeyboardDismissListener() {
      @Override
      public void onKeyboardDismiss() {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(null);
        }
      }
    };

    view.setOnKeyboardDismissListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override
      protected void onUnsubscribe() {
        // TODO: set to null once http://b.android.com/187101 is released.
        view.setOnKeyboardDismissListener(new SearchEditText.OnKeyboardDismissListener() {
          @Override
          public void onKeyboardDismiss() {
          }
        });
      }
    });
  }
}
