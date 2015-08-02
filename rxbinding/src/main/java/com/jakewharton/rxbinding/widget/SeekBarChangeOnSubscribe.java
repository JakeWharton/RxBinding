package com.jakewharton.rxbinding.widget;

import android.widget.SeekBar;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import rx.Observable;
import rx.Subscriber;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class SeekBarChangeOnSubscribe implements Observable.OnSubscribe<Integer> {
  private final SeekBar view;

  public SeekBarChangeOnSubscribe(SeekBar view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Integer> subscriber) {
    checkUiThread();

    SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(progress);
        }
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {
      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {
      }
    };

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnSeekBarChangeListener(null);
      }
    });

    view.setOnSeekBarChangeListener(listener);

    // Emit initial value.
    subscriber.onNext(view.getProgress());
  }
}
