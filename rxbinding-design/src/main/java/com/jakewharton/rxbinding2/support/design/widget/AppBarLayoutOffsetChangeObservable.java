package com.jakewharton.rxbinding2.support.design.widget;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.AppBarLayout.OnOffsetChangedListener;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

final class AppBarLayoutOffsetChangeObservable extends Observable<Integer> {
  private final AppBarLayout view;

  AppBarLayoutOffsetChangeObservable(AppBarLayout view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super Integer> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.addOnOffsetChangedListener(listener);
  }

  static final class Listener extends MainThreadDisposable implements OnOffsetChangedListener {
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
