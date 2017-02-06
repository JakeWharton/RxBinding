package com.jakewharton.rxbinding2.support.v7.widget;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.view.MenuItem;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

final class PopupMenuItemClickObservable extends Observable<MenuItem> {
  private final PopupMenu view;

  PopupMenuItemClickObservable(PopupMenu view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super MenuItem> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.setOnMenuItemClickListener(listener);
  }

  static final class Listener extends MainThreadDisposable implements OnMenuItemClickListener {
    private final PopupMenu popupMenu;
    private final Observer<? super MenuItem> observer;

    Listener(PopupMenu popupMenu, Observer<? super MenuItem> observer) {
      this.popupMenu = popupMenu;
      this.observer = observer;
    }

    @Override public boolean onMenuItemClick(MenuItem item) {
      if (!isDisposed()) {
        observer.onNext(item);
      }
      return true;
    }

    @Override protected void onDispose() {
      popupMenu.setOnMenuItemClickListener(null);
    }
  }
}
