package com.jakewharton.rxbinding2.support.v7.widget;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class ToolbarItemClickObservable extends Observable<MenuItem> {
  private final Toolbar view;

  ToolbarItemClickObservable(Toolbar view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super MenuItem> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.setOnMenuItemClickListener(listener);
  }

  static final class Listener extends MainThreadDisposable implements Toolbar.OnMenuItemClickListener {
    private final Toolbar toolbar;
    private final Observer<? super MenuItem> observer;

    Listener(Toolbar toolbar, Observer<? super MenuItem> observer) {
      this.toolbar = toolbar;
      this.observer = observer;
    }

    @Override public boolean onMenuItemClick(MenuItem item) {
      if (!isDisposed()) {
        observer.onNext(item);
      }
      return true;
    }

    @Override protected void onDispose() {
      toolbar.setOnMenuItemClickListener(null);
    }
  }
}
