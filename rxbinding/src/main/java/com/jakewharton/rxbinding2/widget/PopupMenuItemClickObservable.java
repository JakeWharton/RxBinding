package com.jakewharton.rxbinding2.widget;

import android.view.MenuItem;
import android.widget.PopupMenu;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class PopupMenuItemClickObservable extends Observable<MenuItem> {
  private final PopupMenu view;

  PopupMenuItemClickObservable(PopupMenu view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super MenuItem> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    view.setOnMenuItemClickListener(listener);
    observer.onSubscribe(listener);
  }

  static final class Listener extends MainThreadDisposable
          implements PopupMenu.OnMenuItemClickListener {

    private final PopupMenu view;
    private final Observer<? super MenuItem> observer;

    Listener(PopupMenu view, Observer<? super MenuItem> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override public boolean onMenuItemClick(MenuItem menuItem) {
      if (!isDisposed()) {
        observer.onNext(menuItem);
        return true;
      }
      return false;
    }

    @Override protected void onDispose() {
      view.setOnMenuItemClickListener(null);
    }
  }
}
