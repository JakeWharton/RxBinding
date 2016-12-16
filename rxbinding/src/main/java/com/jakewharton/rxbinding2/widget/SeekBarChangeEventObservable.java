package com.jakewharton.rxbinding2.widget;

import android.widget.SeekBar;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class SeekBarChangeEventObservable extends Observable<SeekBarChangeEvent> {
  private final SeekBar view;

  SeekBarChangeEventObservable(SeekBar view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super SeekBarChangeEvent> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    view.setOnSeekBarChangeListener(listener);
    observer.onSubscribe(listener);
    observer.onNext(SeekBarProgressChangeEvent.create(view, view.getProgress(), false));
  }

  static final class Listener extends MainThreadDisposable
          implements SeekBar.OnSeekBarChangeListener {

    private final SeekBar view;
    private final Observer<? super SeekBarChangeEvent> observer;

    Listener(SeekBar view, Observer<? super SeekBarChangeEvent> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
      if (!isDisposed()) {
        observer.onNext(SeekBarProgressChangeEvent.create(seekBar, progress, fromUser));
      }
    }

    @Override public void onStartTrackingTouch(SeekBar seekBar) {
      if (!isDisposed()) {
        observer.onNext(SeekBarStartChangeEvent.create(seekBar));
      }
    }

    @Override public void onStopTrackingTouch(SeekBar seekBar) {
      if (!isDisposed()) {
        observer.onNext(SeekBarStopChangeEvent.create(seekBar));
      }
    }

    @Override protected void onDispose() {
      view.setOnSeekBarChangeListener(null);
    }
  }
}
