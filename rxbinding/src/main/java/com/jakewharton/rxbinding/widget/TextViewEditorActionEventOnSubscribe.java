package com.jakewharton.rxbinding.widget;

import android.view.KeyEvent;
import android.widget.TextView;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;
import rx.functions.Func1;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class TextViewEditorActionEventOnSubscribe
    implements Observable.OnSubscribe<TextViewEditorActionEvent> {
  final TextView view;
  final Func1<? super TextViewEditorActionEvent, Boolean> handled;

  TextViewEditorActionEventOnSubscribe(TextView view,
      Func1<? super TextViewEditorActionEvent, Boolean> handled) {
    this.view = view;
    this.handled = handled;
  }

  @Override public void call(final Subscriber<? super TextViewEditorActionEvent> subscriber) {
    verifyMainThread();

    TextView.OnEditorActionListener listener = new TextView.OnEditorActionListener() {
      @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
        TextViewEditorActionEvent event = TextViewEditorActionEvent.create(v, actionId, keyEvent);
        if (handled.call(event)) {
          if (!subscriber.isUnsubscribed()) {
            subscriber.onNext(event);
          }
          return true;
        }
        return false;
      }
    };
    view.setOnEditorActionListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnEditorActionListener(null);
      }
    });
  }
}
