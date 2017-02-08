package com.jakewharton.rxbinding2.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import com.jakewharton.rxbinding2.InitialValueObservable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

final class TextViewBeforeTextChangeEventObservable
    extends InitialValueObservable<TextViewBeforeTextChangeEvent> {
  private final TextView view;

  TextViewBeforeTextChangeEventObservable(TextView view) {
    this.view = view;
  }

  @Override
  protected void subscribeListener(Observer<? super TextViewBeforeTextChangeEvent> observer) {
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.addTextChangedListener(listener);
  }

  @Override protected TextViewBeforeTextChangeEvent getInitialValue() {
    return TextViewBeforeTextChangeEvent.create(view, view.getText(), 0, 0, 0);
  }

  static final class Listener extends MainThreadDisposable implements TextWatcher {
    private final TextView view;
    private final Observer<? super TextViewBeforeTextChangeEvent> observer;

    Listener(TextView view, Observer<? super TextViewBeforeTextChangeEvent> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      if (!isDisposed()) {
        observer.onNext(TextViewBeforeTextChangeEvent.create(view, s, start, count, after));
      }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
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
