package com.jakewharton.rxbinding2.support.v7.widget;

import android.support.v7.widget.ActionMenuView;
import android.view.MenuItem;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class ActionMenuViewItemClickObservable extends Observable<MenuItem> {
  private final ActionMenuView view;

  ActionMenuViewItemClickObservable(ActionMenuView view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super MenuItem> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.setOnMenuItemClickListener(listener);
  }

  static final class Listener extends MainThreadDisposable implements ActionMenuView.OnMenuItemClickListener {
    private final ActionMenuView actionMenuView;
    private final Observer<? super MenuItem> observer;

    Listener(ActionMenuView actionMenuView, Observer<? super MenuItem> observer) {
      this.actionMenuView = actionMenuView;
      this.observer = observer;
    }

    @Override public boolean onMenuItemClick(MenuItem item) {
      if (!isDisposed()) {
        observer.onNext(item);
      }
      return true;
    }

    @Override protected void onDispose() {
      actionMenuView.setOnMenuItemClickListener(null);
    }
  }
}
