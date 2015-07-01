package com.jakewharton.rxbinding.widget;

import android.view.View;
import android.widget.AdapterView;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import com.jakewharton.rxbinding.internal.AndroidSubscriptions;
import rx.functions.Action0;
import rx.functions.Func0;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class AdapterViewItemLongClickOnSubscribe implements Observable.OnSubscribe<Integer> {
  private final AdapterView<?> view;
  private final Func0<Boolean> handled;

  public AdapterViewItemLongClickOnSubscribe(AdapterView<?> view, Func0<Boolean> handled) {
    this.view = view;
    this.handled = handled;
  }

  @Override public void call(final Subscriber<? super Integer> subscriber) {
    checkUiThread();

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

    Subscription subscription = AndroidSubscriptions.unsubscribeOnMainThread(new Action0() {
      @Override public void call() {
        view.setOnItemLongClickListener(null);
      }
    });
    subscriber.add(subscription);

    view.setOnItemLongClickListener(listener);
  }
}
