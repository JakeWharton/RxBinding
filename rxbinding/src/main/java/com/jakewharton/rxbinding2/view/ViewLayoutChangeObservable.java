package com.jakewharton.rxbinding2.view;

import android.view.View;
import android.view.View.OnLayoutChangeListener;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;
import kotlin.Unit;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

final class ViewLayoutChangeObservable extends Observable<Object> {
  private final View view;

  ViewLayoutChangeObservable(View view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super Object> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.addOnLayoutChangeListener(listener);
  }

  static final class Listener extends MainThreadDisposable implements OnLayoutChangeListener {
    private final View view;
    private final Observer<? super Object> observer;

    Listener(View view, Observer<? super Object> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft,
        int oldTop, int oldRight, int oldBottom) {
      if (!isDisposed()) {
        observer.onNext(Unit.INSTANCE);
      }
    }

    @Override protected void onDispose() {
      view.removeOnLayoutChangeListener(this);
    }
  }
}
