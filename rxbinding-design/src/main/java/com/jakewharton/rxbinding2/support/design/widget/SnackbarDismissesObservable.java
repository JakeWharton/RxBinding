package com.jakewharton.rxbinding2.support.design.widget;

import android.support.design.widget.Snackbar;
import android.support.design.widget.Snackbar.Callback;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class SnackbarDismissesObservable extends Observable<Integer> {
  private final Snackbar view;

  SnackbarDismissesObservable(Snackbar view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super Integer> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.setCallback(listener.callback);
  }

  final class Listener extends MainThreadDisposable {
    private final Snackbar snackbar;
    private final Callback callback;

    Listener(Snackbar snackbar, final Observer<? super Integer> observer) {
      this.snackbar = snackbar;
      this.callback = new Callback() {
        @Override public void onDismissed(Snackbar snackbar, int event) {
          if (!isDisposed()) {
            observer.onNext(event);
          }
        }
      };
    }

    @Override protected void onDispose() {
      snackbar.setCallback(null);
    }
  }
}
