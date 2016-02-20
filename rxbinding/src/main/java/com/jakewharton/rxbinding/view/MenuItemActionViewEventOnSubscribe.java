package com.jakewharton.rxbinding.view;

import android.view.MenuItem;
import com.jakewharton.rxbinding.view.MenuItemActionViewEvent.Kind;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;
import rx.functions.Func1;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class MenuItemActionViewEventOnSubscribe
    implements Observable.OnSubscribe<MenuItemActionViewEvent> {
  final MenuItem menuItem;
  final Func1<? super MenuItemActionViewEvent, Boolean> handled;

  MenuItemActionViewEventOnSubscribe(MenuItem menuItem,
      Func1<? super MenuItemActionViewEvent, Boolean> handled) {
    this.menuItem = menuItem;
    this.handled = handled;
  }

  @Override public void call(final Subscriber<? super MenuItemActionViewEvent> subscriber) {
    verifyMainThread();

    MenuItem.OnActionExpandListener listener = new MenuItem.OnActionExpandListener() {
      @Override public boolean onMenuItemActionExpand(MenuItem item) {
        MenuItemActionViewEvent event = MenuItemActionViewEvent.create(menuItem, Kind.EXPAND);
        return onEvent(event);
      }

      @Override public boolean onMenuItemActionCollapse(MenuItem item) {
        MenuItemActionViewEvent event = MenuItemActionViewEvent.create(menuItem, Kind.COLLAPSE);
        return onEvent(event);
      }

      private boolean onEvent(MenuItemActionViewEvent event) {
        if (handled.call(event)) {
          if (!subscriber.isUnsubscribed()) {
            subscriber.onNext(event);
          }
          return true;
        }
        return false;
      }
    };

    menuItem.setOnActionExpandListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        menuItem.setOnActionExpandListener(null);
      }
    });
  }
}
