package com.jakewharton.rxbinding.widget;

import android.widget.RadioGroup;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import rx.Observable;
import rx.Subscriber;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class RadioGroupCheckedChangeEventOnSubscribe
    implements Observable.OnSubscribe<RadioGroupCheckedChangeEvent> {
  private final RadioGroup view;

  public RadioGroupCheckedChangeEventOnSubscribe(RadioGroup view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super RadioGroupCheckedChangeEvent> subscriber) {
    checkUiThread();

    RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(RadioGroupCheckedChangeEvent.create(group, checkedId));
        }
      }
    };

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnCheckedChangeListener(null);
      }
    });

    view.setOnCheckedChangeListener(listener);

    // Emit initial value.
    subscriber.onNext(RadioGroupCheckedChangeEvent.create(view, view.getCheckedRadioButtonId()));
  }
}
