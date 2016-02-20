package com.jakewharton.rxbinding.widget;

import android.support.annotation.Nullable;
import android.widget.SeekBar;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class SeekBarChangeOnSubscribe implements Observable.OnSubscribe<Integer> {
  final SeekBar view;
  @Nullable final Boolean shouldBeFromUser;

  public SeekBarChangeOnSubscribe(SeekBar view, @Nullable Boolean shouldBeFromUser) {
    this.view = view;
    this.shouldBeFromUser = shouldBeFromUser;
  }

  @Override public void call(final Subscriber<? super Integer> subscriber) {
    verifyMainThread();

    SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!subscriber.isUnsubscribed()
            && (shouldBeFromUser == null || shouldBeFromUser == fromUser)) {
          subscriber.onNext(progress);
        }
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {
      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {
      }
    };
    view.setOnSeekBarChangeListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnSeekBarChangeListener(null);
      }
    });

    // Emit initial value.
    subscriber.onNext(view.getProgress());
  }
}
