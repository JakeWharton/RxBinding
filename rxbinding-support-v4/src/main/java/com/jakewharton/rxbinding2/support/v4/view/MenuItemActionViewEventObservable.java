package com.jakewharton.rxbinding2.support.v4.view;

import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.view.MenuItem;
import com.jakewharton.rxbinding2.view.MenuItemActionViewEvent;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.functions.Predicate;

import static android.support.v4.view.MenuItemCompat.setOnActionExpandListener;
import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class MenuItemActionViewEventObservable extends Observable<MenuItemActionViewEvent> {
  private final MenuItem menuItem;
  private final Predicate<? super MenuItemActionViewEvent> handled;

  MenuItemActionViewEventObservable(MenuItem menuItem,
      Predicate<? super MenuItemActionViewEvent> handled) {
    this.menuItem = menuItem;
    this.handled = handled;
  }

  @Override protected void subscribeActual(Observer<? super MenuItemActionViewEvent> observer) {
    verifyMainThread();
    Listener listenter = new Listener(menuItem, handled, observer);
    observer.onSubscribe(listenter);
    setOnActionExpandListener(menuItem, listenter);
  }

  static final class Listener extends MainThreadDisposable implements OnActionExpandListener {
    private final MenuItem menuItem;
    private final Predicate<? super MenuItemActionViewEvent> handled;
    private final Observer<? super MenuItemActionViewEvent> observer;

    Listener(MenuItem menuItem, Predicate<? super MenuItemActionViewEvent> handled,
        Observer<? super MenuItemActionViewEvent> observer) {
      this.menuItem = menuItem;
      this.handled = handled;
      this.observer = observer;
    }

    @Override public boolean onMenuItemActionExpand(MenuItem item) {
      MenuItemActionViewEvent event =
          MenuItemActionViewEvent.create(menuItem, MenuItemActionViewEvent.Kind.EXPAND);
      return onEvent(event);
    }

    @Override public boolean onMenuItemActionCollapse(MenuItem item) {
      MenuItemActionViewEvent event =
          MenuItemActionViewEvent.create(menuItem, MenuItemActionViewEvent.Kind.COLLAPSE);
      return onEvent(event);
    }

    private boolean onEvent(MenuItemActionViewEvent event) {
      if (!isDisposed()) {
        try {
          if (handled.test(event)) {
            observer.onNext(event);
            return true;
          }
        } catch (Exception e) {
          observer.onError(e);
          dispose();
        }
      }
      return false;
    }

    @Override protected void onDispose() {
      setOnActionExpandListener(menuItem, null);
    }
  }
}
