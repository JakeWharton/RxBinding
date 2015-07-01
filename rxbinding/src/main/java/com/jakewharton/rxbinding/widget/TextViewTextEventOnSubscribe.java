package com.jakewharton.rxbinding.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import rx.Observable;
import rx.Subscriber;

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
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(TextViewTextChangeEvent.create(view, s, start, before, count));
        }
      }

      @Override public void afterTextChanged(Editable s) {
      }
    };

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.removeTextChangedListener(watcher);
      }
    });

    view.addTextChangedListener(watcher);

    // Send out the initial value.
    subscriber.onNext(TextViewTextChangeEvent.create(view, view.getText(), 0, 0, 0));
  }
}
