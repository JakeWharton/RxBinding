package com.jakewharton.rxbinding.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import rx.Observable;
import rx.Subscriber;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class TextViewBeforeTextChangeEventOnSubscribe
    implements Observable.OnSubscribe<TextViewBeforeTextChangeEvent> {
  private final TextView view;

  public TextViewBeforeTextChangeEventOnSubscribe(TextView view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super TextViewBeforeTextChangeEvent> subscriber) {
    checkUiThread();

    final TextWatcher watcher = new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(TextViewBeforeTextChangeEvent.create(view, s, start, count, after));
        }
      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override public void afterTextChanged(Editable s) {
      }
    };
    view.addTextChangedListener(watcher);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.removeTextChangedListener(watcher);
      }
    });

    // Emit initial value.
    subscriber.onNext(TextViewBeforeTextChangeEvent.create(view, view.getText(), 0, 0, 0));
  }
}
