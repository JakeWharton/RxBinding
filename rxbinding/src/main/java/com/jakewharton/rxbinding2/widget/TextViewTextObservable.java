package com.jakewharton.rxbinding2.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class TextViewTextObservable extends Observable<CharSequence> {
  private final TextView view;

  TextViewTextObservable(TextView view) {
    this.view = view;
  }

  @Override
  protected void subscribeActual(Observer<? super CharSequence> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.addTextChangedListener(listener);
    observer.onNext(view.getText());
  }

  final static class Listener extends MainThreadDisposable implements TextWatcher {
    private final TextView view;
    private final Observer<? super CharSequence> observer;

    Listener(TextView view, Observer<? super CharSequence> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
      if (!isDisposed()) {
        observer.onNext(s);
      }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    protected void onDispose() {
      view.removeTextChangedListener(this);
    }
  }
}
