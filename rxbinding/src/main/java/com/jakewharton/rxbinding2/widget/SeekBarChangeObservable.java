package com.jakewharton.rxbinding2.widget;

import android.support.annotation.Nullable;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.jakewharton.rxbinding2.InitialValueObservable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

final class SeekBarChangeObservable extends InitialValueObservable<Integer> {
  private final SeekBar view;
  @Nullable private final Boolean shouldBeFromUser;

  SeekBarChangeObservable(SeekBar view, @Nullable Boolean shouldBeFromUser) {
    this.view = view;
    this.shouldBeFromUser = shouldBeFromUser;
  }

  @Override protected void subscribeListener(Observer<? super Integer> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(view, shouldBeFromUser, observer);
    view.setOnSeekBarChangeListener(listener);
    observer.onSubscribe(listener);
  }

  @Override protected Integer getInitialValue() {
    return view.getProgress();
  }

  static final class Listener extends MainThreadDisposable implements OnSeekBarChangeListener {
    private final SeekBar view;
    private final Boolean shouldBeFromUser;
    private final Observer<? super Integer> observer;

    Listener(SeekBar view, Boolean shouldBeFromUser, Observer<? super Integer> observer) {
      this.view = view;
      this.shouldBeFromUser = shouldBeFromUser;
      this.observer = observer;
    }

    @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
      if (!isDisposed() && (shouldBeFromUser == null || shouldBeFromUser == fromUser)) {
        observer.onNext(progress);
      }
    }

    @Override public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override protected void onDispose() {
      view.setOnSeekBarChangeListener(this);
    }
  }
}
