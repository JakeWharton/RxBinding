package com.jakewharton.rxbinding2.support.design.widget;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.View.OnClickListener;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

final class SnackbarActionObservable extends Observable<View> {
  private final Snackbar view;
  private final int resId;
  private final CharSequence text;

  SnackbarActionObservable(Snackbar view, int resId) {
    this.view = view;
    this.text = null;
    this.resId = resId;
  }
  SnackbarActionObservable(Snackbar view, CharSequence text) {
    this.view = view;
    this.text = text;
    this.resId = -1;
  }

  @Override protected void subscribeActual(Observer<? super View> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
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
    final OnClickListener callback;

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
