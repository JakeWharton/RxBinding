package com.jakewharton.rxbinding2.widget;

import android.widget.RadioGroup;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class RadioGroupCheckedChangeObservable extends Observable<Integer> {
  private final RadioGroup view;

  RadioGroupCheckedChangeObservable(RadioGroup view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super Integer> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    view.setOnCheckedChangeListener(listener);
    observer.onSubscribe(listener);
    observer.onNext(view.getCheckedRadioButtonId());
  }

  static final class Listener extends MainThreadDisposable
          implements RadioGroup.OnCheckedChangeListener {

    private final RadioGroup view;
    private final Observer<? super Integer> observer;

    Listener(RadioGroup view, Observer<? super Integer> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
      if (!isDisposed()) {
        observer.onNext(checkedId);
      }
    }

    @Override protected void onDispose() {
      view.setOnCheckedChangeListener(null);
    }
  }
}
