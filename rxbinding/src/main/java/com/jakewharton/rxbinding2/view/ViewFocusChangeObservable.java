package com.jakewharton.rxbinding2.view;

import android.view.View;
import android.view.View.OnFocusChangeListener;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class ViewFocusChangeObservable extends Observable<Boolean> {
  private final View view;

  ViewFocusChangeObservable(View view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super Boolean> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.setOnFocusChangeListener(listener);
    observer.onNext(view.hasFocus());
  }

  static final class Listener extends MainThreadDisposable implements OnFocusChangeListener {
    private final View view;
    private final Observer<? super Boolean> observer;

    Listener(View view, Observer<? super Boolean> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override public void onFocusChange(View v, boolean hasFocus) {
      if (!isDisposed()) {
        observer.onNext(hasFocus);
      }
    }

    @Override protected void onDispose() {
      view.setOnFocusChangeListener(null);
    }
  }
}
