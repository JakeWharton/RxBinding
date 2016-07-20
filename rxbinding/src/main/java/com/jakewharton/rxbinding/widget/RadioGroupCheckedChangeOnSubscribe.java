package com.jakewharton.rxbinding.widget;

import android.widget.RadioGroup;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class RadioGroupCheckedChangeOnSubscribe implements Observable.OnSubscribe<Integer> {
  final RadioGroup view;

  public RadioGroupCheckedChangeOnSubscribe(RadioGroup view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Integer> subscriber) {
    verifyMainThread();

    RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(checkedId);
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
    subscriber.onNext(view.getCheckedRadioButtonId());
  }
}
