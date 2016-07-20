package com.jakewharton.rxbinding.widget;

import android.view.View;
import android.widget.AdapterView;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static android.widget.AdapterView.INVALID_POSITION;
import static rx.android.MainThreadSubscription.verifyMainThread;

final class AdapterViewItemSelectionOnSubscribe implements Observable.OnSubscribe<Integer> {
  final AdapterView<?> view;

  public AdapterViewItemSelectionOnSubscribe(AdapterView<?> view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Integer> subscriber) {
    verifyMainThread();

    AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(position);
        }
      }

      @Override public void onNothingSelected(AdapterView<?> parent) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(INVALID_POSITION);
        }
      }
    };
    view.setOnItemSelectedListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnItemSelectedListener(null);
      }
    });

    // Emit initial value.
    subscriber.onNext(view.getSelectedItemPosition());
  }
}
