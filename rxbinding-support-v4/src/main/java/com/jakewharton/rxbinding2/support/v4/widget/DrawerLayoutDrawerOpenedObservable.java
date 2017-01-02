package com.jakewharton.rxbinding2.support.v4.widget;

import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.View;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class DrawerLayoutDrawerOpenedObservable extends Observable<Boolean> {
  private final DrawerLayout view;
  private final int gravity;

  DrawerLayoutDrawerOpenedObservable(DrawerLayout view, int gravity) {
    this.view = view;
    this.gravity = gravity;
  }

  @Override protected void subscribeActual(Observer<? super Boolean> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, gravity, observer);
    observer.onSubscribe(listener);
    view.addDrawerListener(listener);
    observer.onNext(view.isDrawerOpen(gravity));
  }

  static final class Listener extends MainThreadDisposable implements DrawerListener {
    private final DrawerLayout view;
    private final int gravity;
    private final Observer<? super Boolean> observer;

    Listener(DrawerLayout view, int gravity, Observer<? super Boolean> observer) {
      this.view = view;
      this.gravity = gravity;
      this.observer = observer;
    }

    @Override public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override public void onDrawerOpened(View drawerView) {
      if (!isDisposed()) {
        int drawerGravity = ((DrawerLayout.LayoutParams) drawerView.getLayoutParams()).gravity;
        if (drawerGravity == gravity) {
          observer.onNext(true);
        }
      }
    }

    @Override public void onDrawerClosed(View drawerView) {
      if (!isDisposed()) {
        int drawerGravity = ((DrawerLayout.LayoutParams) drawerView.getLayoutParams()).gravity;
        if (drawerGravity == gravity) {
          observer.onNext(false);
        }
      }
    }

    @Override public void onDrawerStateChanged(int newState) {

    }

    @Override protected void onDispose() {
      view.removeDrawerListener(this);
    }
  }
}
