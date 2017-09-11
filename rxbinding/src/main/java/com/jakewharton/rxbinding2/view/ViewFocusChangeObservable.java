package com.jakewharton.rxbinding2.view;

import android.view.View;
import android.view.View.OnFocusChangeListener;
import com.jakewharton.rxbinding2.InitialValueObservable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

final class ViewFocusChangeObservable extends InitialValueObservable<Boolean> {
  private final View view;

  ViewFocusChangeObservable(View view) {
    this.view = view;
  }

  @Override protected void subscribeListener(Observer<? super Boolean> observer) {
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.setOnFocusChangeListener(listener);
  }

  @Override protected Boolean getInitialValue() {
    return view.hasFocus();
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
