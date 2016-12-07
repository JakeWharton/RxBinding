package com.jakewharton.rxbinding2.view;

import android.view.View;
import android.view.View.OnLayoutChangeListener;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class ViewLayoutChangeEventObservable extends Observable<ViewLayoutChangeEvent> {
  private final View view;

  ViewLayoutChangeEventObservable(View view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super ViewLayoutChangeEvent> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.addOnLayoutChangeListener(listener);
  }

  static final class Listener extends MainThreadDisposable implements OnLayoutChangeListener {
    private final View view;
    private final Observer<? super ViewLayoutChangeEvent> observer;

    Listener(View view, Observer<? super ViewLayoutChangeEvent> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft,
        int oldTop, int oldRight, int oldBottom) {
      if (!isDisposed()) {
        observer.onNext(
            ViewLayoutChangeEvent.create(v, left, top, right, bottom, oldLeft, oldTop, oldRight,
                oldBottom));
      }
    }

    @Override protected void onDispose() {
      view.removeOnLayoutChangeListener(this);
    }
  }
}
