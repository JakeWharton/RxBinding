package com.jakewharton.rxbinding.widget;

import android.widget.SeekBar;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import rx.Observable;
import rx.Subscriber;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class SeekBarChangeEventOnSubscribe
    implements Observable.OnSubscribe<SeekBarChangeEvent> {
  private final SeekBar view;

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

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnSeekBarChangeListener(null);
      }
    });

    view.setOnSeekBarChangeListener(listener);

    // Emit initial value.
    subscriber.onNext(SeekBarProgressChangeEvent.create(view, view.getProgress(), false));
  }
}
