package com.jakewharton.rxbinding2.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class TextViewTextChangeEventObservable extends Observable<TextViewTextChangeEvent> {
  private final TextView view;

  TextViewTextChangeEventObservable(TextView view) {
    this.view = view;
  }

  @Override
  protected void subscribeActual(Observer<? super TextViewTextChangeEvent> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.addTextChangedListener(listener);
    observer.onNext(TextViewTextChangeEvent.create(view, view.getText(), 0, 0, 0));
  }

  final static class Listener extends MainThreadDisposable implements TextWatcher {
    private final TextView view;
    private final Observer<? super TextViewTextChangeEvent> observer;

    Listener(TextView view, Observer<? super TextViewTextChangeEvent> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
      if (!isDisposed()) {
        observer.onNext(TextViewTextChangeEvent.create(view, s, start, before, count));
      }
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    @Override
    protected void onDispose() {
      view.removeTextChangedListener(this);
    }
  }
}
