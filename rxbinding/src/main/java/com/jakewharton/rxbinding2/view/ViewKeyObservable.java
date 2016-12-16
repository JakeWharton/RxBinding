package com.jakewharton.rxbinding2.view;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.functions.Predicate;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class ViewKeyObservable extends Observable<KeyEvent> {
  private final View view;
  private final Predicate<? super KeyEvent> handled;

  ViewKeyObservable(View view, Predicate<? super KeyEvent> handled) {
    this.view = view;
    this.handled = handled;
  }

  @Override protected void subscribeActual(Observer<? super KeyEvent> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, handled, observer);
    observer.onSubscribe(listener);
    view.setOnKeyListener(listener);
  }

  static final class Listener extends MainThreadDisposable implements OnKeyListener {
    private final View view;
    private final Predicate<? super KeyEvent> handled;
    private final Observer<? super KeyEvent> observer;

    Listener(View view, Predicate<? super KeyEvent> handled,
        Observer<? super KeyEvent> observer) {
      this.view = view;
      this.handled = handled;
      this.observer = observer;
    }

    @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
      if (!isDisposed()) {
        try {
          if (handled.test(event)) {
            observer.onNext(event);
            return true;
          }
        } catch (Exception e) {
          observer.onError(e);
          dispose();
        }
      }
      return false;
    }

    @Override protected void onDispose() {
      view.setOnKeyListener(null);
    }
  }
}
