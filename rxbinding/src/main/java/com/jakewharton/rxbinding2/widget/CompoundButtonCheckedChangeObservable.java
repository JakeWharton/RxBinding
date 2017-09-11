package com.jakewharton.rxbinding2.widget;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.jakewharton.rxbinding2.InitialValueObservable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

final class CompoundButtonCheckedChangeObservable extends InitialValueObservable<Boolean> {
  private final CompoundButton view;

  CompoundButtonCheckedChangeObservable(CompoundButton view) {
    this.view = view;
  }

  @Override protected void subscribeListener(Observer<? super Boolean> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.setOnCheckedChangeListener(listener);
  }

  @Override protected Boolean getInitialValue() {
    return view.isChecked();
  }

  static final class Listener extends MainThreadDisposable implements OnCheckedChangeListener {
    private final CompoundButton view;
    private final Observer<? super Boolean> observer;

    Listener(CompoundButton view, Observer<? super Boolean> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
      if (!isDisposed()) {
        observer.onNext(isChecked);
      }
    }

    @Override
    protected void onDispose() {
      view.setOnCheckedChangeListener(null);
    }
  }
}
