package com.jakewharton.rxbinding2.view;

import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.functions.Function;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class MenuItemActionViewEventObservable extends Observable<MenuItemActionViewEvent> {
  private final MenuItem menuItem;
  private final Function<? super MenuItemActionViewEvent, Boolean> handled;

  MenuItemActionViewEventObservable(MenuItem menuItem, Function<? super MenuItemActionViewEvent, Boolean> handled) {
    this.menuItem = menuItem;
    this.handled = handled;
  }

  @Override protected void subscribeActual(Observer<? super MenuItemActionViewEvent> observer) {
    verifyMainThread();
    Listener listener = new Listener(menuItem, handled, observer);
    observer.onSubscribe(listener);
    menuItem.setOnActionExpandListener(listener);
  }

  static final class Listener extends MainThreadDisposable implements OnActionExpandListener {
    private final MenuItem menuItem;
    private final Function<? super MenuItemActionViewEvent, Boolean> handled;
    private final Observer<? super MenuItemActionViewEvent> observer;

    Listener(MenuItem menuItem, Function<? super MenuItemActionViewEvent, Boolean> handled,
        Observer<? super MenuItemActionViewEvent> observer) {
      this.menuItem = menuItem;
      this.handled = handled;
      this.observer = observer;
    }

    @Override public boolean onMenuItemActionExpand(MenuItem item) {
      MenuItemActionViewEvent event = MenuItemActionViewEvent.create(menuItem, MenuItemActionViewEvent.Kind.EXPAND);
      return onEvent(event);
    }

    @Override public boolean onMenuItemActionCollapse(MenuItem item) {
      MenuItemActionViewEvent event = MenuItemActionViewEvent.create(menuItem, MenuItemActionViewEvent.Kind.COLLAPSE);
      return onEvent(event);
    }

    private boolean onEvent(MenuItemActionViewEvent event) {
      if (!isDisposed()) {
        try {
          if (handled.apply(event)) {
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
      menuItem.setOnActionExpandListener(null);
    }
  }
}
