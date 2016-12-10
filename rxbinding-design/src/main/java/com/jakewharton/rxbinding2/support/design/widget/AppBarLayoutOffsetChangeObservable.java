package com.jakewharton.rxbinding2.support.design.widget;

import android.support.design.widget.AppBarLayout;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class AppBarLayoutOffsetChangeObservable extends Observable<Integer> {
  private final AppBarLayout view;

  AppBarLayoutOffsetChangeObservable(AppBarLayout view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super Integer> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.addOnOffsetChangedListener(listener);
  }

  static final class Listener extends MainThreadDisposable implements AppBarLayout.OnOffsetChangedListener {
    private final AppBarLayout appBarLayout;
    private final Observer<? super Integer> observer;

    Listener(AppBarLayout appBarLayout, Observer<? super Integer> observer) {
      this.appBarLayout = appBarLayout;
      this.observer = observer;
    }

    @Override public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
      if (!isDisposed()) {
        observer.onNext(verticalOffset);
      }
    }

    @Override protected void onDispose() {
      appBarLayout.removeOnOffsetChangedListener(this);
    }
  }
}
