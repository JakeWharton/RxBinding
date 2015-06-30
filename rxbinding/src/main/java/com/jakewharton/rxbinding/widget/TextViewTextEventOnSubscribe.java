package com.jakewharton.rxbinding.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import com.jakewharton.rxbinding.internal.AndroidSubscriptions;
import rx.functions.Action0;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class TextViewTextEventOnSubscribe
    implements Observable.OnSubscribe<TextViewTextChangeEvent> {
  private final TextView view;

  public TextViewTextEventOnSubscribe(TextView view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super TextViewTextChangeEvent> subscriber) {
    checkUiThread();

    final TextWatcher watcher = new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
        subscriber.onNext(TextViewTextChangeEvent.create(view, s, start, before, count));
      }

      @Override public void afterTextChanged(Editable s) {
      }
    };

    Subscription subscription = AndroidSubscriptions.unsubscribeOnMainThread(new Action0() {
      @Override public void call() {
        view.removeTextChangedListener(watcher);
      }
    });
    subscriber.add(subscription);

    view.addTextChangedListener(watcher);

    // Send out the initial value.
    subscriber.onNext(TextViewTextChangeEvent.create(view, view.getText(), 0, 0, 0));
  }
}
