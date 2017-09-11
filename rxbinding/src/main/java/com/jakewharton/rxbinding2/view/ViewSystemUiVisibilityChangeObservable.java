package com.jakewharton.rxbinding2.view;

import android.view.View;
import android.view.View.OnSystemUiVisibilityChangeListener;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

final class ViewSystemUiVisibilityChangeObservable extends Observable<Integer> {
  private final View view;

  ViewSystemUiVisibilityChangeObservable(View view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super Integer> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.setOnSystemUiVisibilityChangeListener(listener);
  }

  static final class Listener extends MainThreadDisposable
      implements OnSystemUiVisibilityChangeListener {
    private final View view;
    private final Observer<? super Integer> observer;

    Listener(View view, Observer<? super Integer> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override public void onSystemUiVisibilityChange(int visibility) {
      if (!isDisposed()) {
        observer.onNext(visibility);
      }
    }

    @Override protected void onDispose() {
      view.setOnSystemUiVisibilityChangeListener(null);
    }
  }
}
