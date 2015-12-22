package com.jakewharton.rxbinding.widget;

import android.widget.SeekBar;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class SeekBarChangeEventOnSubscribe
    implements Observable.OnSubscribe<SeekBarChangeEvent> {
  final SeekBar view;

  public SeekBarChangeEventOnSubscribe(SeekBar view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super SeekBarChangeEvent> subscriber) {
    checkUiThread();

    SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(SeekBarProgressChangeEvent.create(seekBar, progress, fromUser));
        }
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(SeekBarStartChangeEvent.create(seekBar));
        }
      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(SeekBarStopChangeEvent.create(seekBar));
        }
      }
    };
    view.setOnSeekBarChangeListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnSeekBarChangeListener(null);
      }
    });

    // Emit initial value.
    subscriber.onNext(SeekBarProgressChangeEvent.create(view, view.getProgress(), false));
  }
}
