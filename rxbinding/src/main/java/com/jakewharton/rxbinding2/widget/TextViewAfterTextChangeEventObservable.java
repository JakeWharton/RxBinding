package com.jakewharton.rxbinding2.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import com.jakewharton.rxbinding2.InitialValueObservable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

final class TextViewAfterTextChangeEventObservable
    extends InitialValueObservable<TextViewAfterTextChangeEvent> {
  private final TextView view;

  TextViewAfterTextChangeEventObservable(TextView view) {
    this.view = view;
  }

  @Override
  protected void subscribeListener(Observer<? super TextViewAfterTextChangeEvent> observer) {
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.addTextChangedListener(listener);
  }

  @Override protected TextViewAfterTextChangeEvent getInitialValue() {
    return TextViewAfterTextChangeEvent.create(view, view.getEditableText());
  }

  static final class Listener extends MainThreadDisposable implements TextWatcher {
    private final TextView view;
    private final Observer<? super TextViewAfterTextChangeEvent> observer;

    Listener(TextView view, Observer<? super TextViewAfterTextChangeEvent> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
      observer.onNext(TextViewAfterTextChangeEvent.create(view, s));
    }

    @Override
    protected void onDispose() {
      view.removeTextChangedListener(this);
    }
  }
}
