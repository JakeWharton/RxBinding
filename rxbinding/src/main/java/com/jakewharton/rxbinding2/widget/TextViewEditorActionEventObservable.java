package com.jakewharton.rxbinding2.widget;

import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.functions.Predicate;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

final class TextViewEditorActionEventObservable extends Observable<TextViewEditorActionEvent> {
  private final TextView view;
  private final Predicate<? super TextViewEditorActionEvent> handled;

  TextViewEditorActionEventObservable(TextView view,
      Predicate<? super TextViewEditorActionEvent> handled) {
    this.view = view;
    this.handled = handled;
  }

  @Override
  protected void subscribeActual(Observer<? super TextViewEditorActionEvent> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(view, observer, handled);
    observer.onSubscribe(listener);
    view.setOnEditorActionListener(listener);
  }

  static final class Listener extends MainThreadDisposable implements OnEditorActionListener {
    private final TextView view;
    private final Observer<? super TextViewEditorActionEvent> observer;
    private final Predicate<? super TextViewEditorActionEvent> handled;

    Listener(TextView view, Observer<? super TextViewEditorActionEvent> observer,
        Predicate<? super TextViewEditorActionEvent> handled) {
      this.view = view;
      this.observer = observer;
      this.handled = handled;
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
      TextViewEditorActionEvent event = TextViewEditorActionEvent.create(view, actionId, keyEvent);
      try {
        if (!isDisposed() && handled.test(event)) {
          observer.onNext(event);
          return true;
        }
      } catch (Exception e) {
        observer.onError(e);
        dispose();
      }
      return false;
    }

    @Override
    protected void onDispose() {
      view.setOnEditorActionListener(null);
    }
  }
}
