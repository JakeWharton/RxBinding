package com.jakewharton.rxbinding2.widget;

import android.view.KeyEvent;
import android.widget.TextView;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.functions.Predicate;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class TextViewEditorActionObservable extends Observable<Integer> {
  private final TextView view;
  private final Predicate<? super Integer> handled;

  TextViewEditorActionObservable(TextView view, Predicate<? super Integer> handled) {
    this.view = view;
    this.handled = handled;
  }

  @Override
  protected void subscribeActual(Observer<? super Integer> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer, handled);
    observer.onSubscribe(listener);
    view.setOnEditorActionListener(listener);
  }

  static final class Listener extends MainThreadDisposable
          implements TextView.OnEditorActionListener {

    private final TextView view;
    private final Observer<? super Integer> observer;
    private final Predicate<? super Integer> handled;

    Listener(TextView view,
             Observer<? super Integer> observer,
             Predicate<? super Integer> handled) {
      this.view = view;
      this.observer = observer;
      this.handled = handled;
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
      try {
        if (!isDisposed() && handled.test(actionId)) {
          observer.onNext(actionId);
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
