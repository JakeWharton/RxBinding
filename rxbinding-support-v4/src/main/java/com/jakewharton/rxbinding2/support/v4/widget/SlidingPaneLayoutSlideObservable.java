package com.jakewharton.rxbinding2.support.v4.widget;

import android.support.v4.widget.SlidingPaneLayout;
import android.view.View;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class SlidingPaneLayoutSlideObservable extends Observable<Float> {
  private final SlidingPaneLayout view;

  SlidingPaneLayoutSlideObservable(SlidingPaneLayout view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super Float> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.setPanelSlideListener(listener);
  }

  static final class Listener extends MainThreadDisposable
      implements SlidingPaneLayout.PanelSlideListener {
    private final SlidingPaneLayout view;
    private final Observer<? super Float> observer;

    Listener(SlidingPaneLayout view, Observer<? super Float> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override public void onPanelSlide(View panel, float slideOffset) {
      if (!isDisposed()) {
        observer.onNext(slideOffset);
      }
    }

    @Override public void onPanelOpened(View panel) {

    }

    @Override public void onPanelClosed(View panel) {

    }

    @Override protected void onDispose() {
      view.setPanelSlideListener(null);
    }
  }
}
