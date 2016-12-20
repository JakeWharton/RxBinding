package com.jakewharton.rxbinding2.widget;

import android.widget.CompoundButton;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class CompoundButtonCheckedChangeObservable extends Observable<Boolean> {

  private final CompoundButton view;

  CompoundButtonCheckedChangeObservable(CompoundButton view) {
    this.view = view;
  }

  @Override
  protected void subscribeActual(Observer<? super Boolean> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.setOnCheckedChangeListener(listener);
    observer.onNext(view.isChecked());
  }

  static final class Listener extends MainThreadDisposable
          implements CompoundButton.OnCheckedChangeListener {

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
