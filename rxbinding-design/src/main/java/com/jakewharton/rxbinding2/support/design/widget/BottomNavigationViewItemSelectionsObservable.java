package com.jakewharton.rxbinding2.support.design.widget;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class BottomNavigationViewItemSelectionsObservable extends Observable<MenuItem> {
  private final BottomNavigationView view;

  BottomNavigationViewItemSelectionsObservable(BottomNavigationView view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super MenuItem> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.setOnNavigationItemSelectedListener(listener);

    // Emit initial item, if one can be found
    Menu menu = view.getMenu();
    for (int i = 0, count = menu.size(); i < count; i++) {
      MenuItem item = menu.getItem(i);
      if (item.isChecked()) {
        observer.onNext(item);
        break;
      }
    }
  }

  static final class Listener extends MainThreadDisposable implements BottomNavigationView.OnNavigationItemSelectedListener {
    private final BottomNavigationView bottomNavigationView;
    private final Observer<? super MenuItem> observer;

    Listener(BottomNavigationView bottomNavigationView, Observer<? super MenuItem> observer) {
      this.bottomNavigationView = bottomNavigationView;
      this.observer = observer;
    }

    @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      if (!isDisposed()) {
        observer.onNext(item);
      }
      return true;
    }

    @Override protected void onDispose() {
      bottomNavigationView.setOnNavigationItemSelectedListener(null);
    }
  }
}
