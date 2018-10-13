package com.jakewharton.rxbinding2.widget;

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.jakewharton.rxbinding2.InitialValueObservable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

final class SeekBarChangeEventObservable extends InitialValueObservable<SeekBarChangeEvent> {
  private final SeekBar view;

  SeekBarChangeEventObservable(SeekBar view) {
    this.view = view;
  }

  @Override protected void subscribeListener(Observer<? super SeekBarChangeEvent> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(view, observer);
    view.setOnSeekBarChangeListener(listener);
    observer.onSubscribe(listener);
  }

  @Override protected SeekBarChangeEvent getInitialValue() {
    return new SeekBarProgressChangeEvent(view, view.getProgress(), false);
  }

  static final class Listener extends MainThreadDisposable implements OnSeekBarChangeListener {
    private final SeekBar view;
    private final Observer<? super SeekBarChangeEvent> observer;

    Listener(SeekBar view, Observer<? super SeekBarChangeEvent> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
      if (!isDisposed()) {
        observer.onNext(new SeekBarProgressChangeEvent(seekBar, progress, fromUser));
      }
    }

    @Override public void onStartTrackingTouch(SeekBar seekBar) {
      if (!isDisposed()) {
        observer.onNext(new SeekBarStartChangeEvent(seekBar));
      }
    }

    @Override public void onStopTrackingTouch(SeekBar seekBar) {
      if (!isDisposed()) {
        observer.onNext(new SeekBarStopChangeEvent(seekBar));
      }
    }

    @Override protected void onDispose() {
      view.setOnSeekBarChangeListener(null);
    }
  }
}
