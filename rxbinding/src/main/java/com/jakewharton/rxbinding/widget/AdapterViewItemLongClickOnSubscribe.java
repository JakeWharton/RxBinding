package com.jakewharton.rxbinding.widget;

import android.view.View;
import android.widget.AdapterView;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;
import rx.functions.Func0;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class AdapterViewItemLongClickOnSubscribe implements Observable.OnSubscribe<Integer> {
  final AdapterView<?> view;
  final Func0<Boolean> handled;

  public AdapterViewItemLongClickOnSubscribe(AdapterView<?> view, Func0<Boolean> handled) {
    this.view = view;
    this.handled = handled;
  }

  @Override public void call(final Subscriber<? super Integer> subscriber) {
    verifyMainThread();

    AdapterView.OnItemLongClickListener listener = new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (handled.call()) {
          if (!subscriber.isUnsubscribed()) {
            subscriber.onNext(position);
          }
          return true;
        }
        return false;
      }
    };
    view.setOnItemLongClickListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnItemLongClickListener(null);
      }
    });
  }
}
