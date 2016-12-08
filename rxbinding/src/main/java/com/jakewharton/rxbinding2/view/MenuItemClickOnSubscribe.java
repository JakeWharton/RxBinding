package com.jakewharton.rxbinding2.view;

import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import com.jakewharton.rxbinding2.internal.Notification;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.functions.Predicate;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class MenuItemClickOnSubscribe extends Observable<Object> {
  private final MenuItem menuItem;
  private final Predicate<? super MenuItem> handled;

  MenuItemClickOnSubscribe(MenuItem menuItem, Predicate<? super MenuItem> handled) {
    this.menuItem = menuItem;
    this.handled = handled;
  }

  @Override protected void subscribeActual(Observer<? super Object> observer) {
    verifyMainThread();
    Listener listener = new Listener(menuItem, handled, observer);
    observer.onSubscribe(listener);
    menuItem.setOnMenuItemClickListener(listener);
  }

  static final class Listener extends MainThreadDisposable implements OnMenuItemClickListener {
    private final MenuItem menuItem;
    private final Predicate<? super MenuItem> handled;
    private final Observer<? super Object> observer;

    Listener(MenuItem menuItem, Predicate<? super MenuItem> handled,
        Observer<? super Object> observer) {
      this.menuItem = menuItem;
      this.handled = handled;
      this.observer = observer;
    }

    @Override public boolean onMenuItemClick(MenuItem item) {
      if (!isDisposed()) {
        try {
          if (handled.test(menuItem)) {
            observer.onNext(Notification.INSTANCE);
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
      menuItem.setOnMenuItemClickListener(null);
    }
  }
}
