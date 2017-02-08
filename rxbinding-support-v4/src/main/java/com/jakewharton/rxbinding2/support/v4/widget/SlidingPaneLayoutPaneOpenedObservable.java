package com.jakewharton.rxbinding2.support.v4.widget;

import android.support.v4.widget.SlidingPaneLayout;
import android.support.v4.widget.SlidingPaneLayout.PanelSlideListener;
import android.view.View;
import com.jakewharton.rxbinding2.InitialValueObservable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

final class SlidingPaneLayoutPaneOpenedObservable extends InitialValueObservable<Boolean> {
  private final SlidingPaneLayout view;

  SlidingPaneLayoutPaneOpenedObservable(SlidingPaneLayout view) {
    this.view = view;
  }

  @Override protected void subscribeListener(Observer<? super Boolean> observer) {
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.setPanelSlideListener(listener);
  }

  @Override protected Boolean getInitialValue() {
    return view.isOpen();
  }

  static final class Listener extends MainThreadDisposable implements PanelSlideListener {
    private final SlidingPaneLayout view;
    private final Observer<? super Boolean> observer;

    Listener(SlidingPaneLayout view, Observer<? super Boolean> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override public void onPanelSlide(View panel, float slideOffset) {

    }

    @Override public void onPanelOpened(View panel) {
      if (!isDisposed()) {
        observer.onNext(true);
      }
    }

    @Override public void onPanelClosed(View panel) {
      if (!isDisposed()) {
        observer.onNext(false);
      }
    }

    @Override protected void onDispose() {
      view.setPanelSlideListener(null);
    }
  }
}
