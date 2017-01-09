package com.jakewharton.rxbinding2.support.design.widget;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.View.OnClickListener;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class SnackbarActionObservable extends Observable<View> {
  final Snackbar view;
  final int resId;
  final CharSequence text;

  public SnackbarActionObservable(Snackbar view, int resId) {
    this.view = view;
    this.text = null;
    this.resId = resId;
  }
  public SnackbarActionObservable(Snackbar view, CharSequence text) {
    this.view = view;
    this.text = text;
    this.resId = -1;
  }

  @Override protected void subscribeActual(Observer<? super View> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    if (text == null) {
      view.setAction(resId, listener.callback);
    } else {
      view.setAction(text, listener.callback);
    }
  }

  final class Listener extends MainThreadDisposable {
    private final Snackbar snackbar;
    private final OnClickListener callback;

    Listener(Snackbar snackbar, final Observer<? super View> observer) {
      this.snackbar = snackbar;
      this.callback = new OnClickListener() {
        @Override
        public void onClick(View view) {
          if (!isDisposed()) {
            observer.onNext(view);
          }
        }
      };
    }

    @Override protected void onDispose() {
      if (text == null) {
        snackbar.setAction(resId, null);
      } else {
        snackbar.setAction(text, null);
      }
    }
  }
}
