package com.jakewharton.rxbinding.widget;

import android.widget.CompoundButton;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class CompoundButtonCheckedChangeOnSubscribe implements Observable.OnSubscribe<Boolean> {
  final CompoundButton view;

  public CompoundButtonCheckedChangeOnSubscribe(CompoundButton view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Boolean> subscriber) {
    verifyMainThread();

    CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(isChecked);
        }
      }
    };
    view.setOnCheckedChangeListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnCheckedChangeListener(null);
      }
    });

    // Emit initial value.
    subscriber.onNext(view.isChecked());
  }
}
