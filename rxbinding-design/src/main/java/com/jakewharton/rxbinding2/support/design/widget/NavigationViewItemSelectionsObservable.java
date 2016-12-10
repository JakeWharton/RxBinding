package com.jakewharton.rxbinding2.support.design.widget;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.view.MenuItem;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class NavigationViewItemSelectionsObservable extends Observable<MenuItem> {
  private final NavigationView view;

  NavigationViewItemSelectionsObservable(NavigationView view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super MenuItem> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.setNavigationItemSelectedListener(listener);

    // Emit initial checked item, if one can be found.
    Menu menu = view.getMenu();
    for (int i = 0, count = menu.size(); i < count; i++) {
      MenuItem item = menu.getItem(i);
      if (item.isChecked()) {
        observer.onNext(item);
        break;
      }
    }
  }

  static final class Listener extends MainThreadDisposable implements NavigationView.OnNavigationItemSelectedListener {
    private final NavigationView navigationView;
    private final Observer<? super MenuItem> observer;

    Listener(NavigationView navigationView, Observer<? super MenuItem> observer) {
      this.navigationView = navigationView;
      this.observer = observer;
    }

    @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      if (!isDisposed()) {
        observer.onNext(item);
      }
      return true;
    }

    @Override protected void onDispose() {
      navigationView.setNavigationItemSelectedListener(null);
    }
  }
}
