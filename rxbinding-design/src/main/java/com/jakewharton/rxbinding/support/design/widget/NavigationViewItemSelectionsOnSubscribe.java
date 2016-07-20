package com.jakewharton.rxbinding.support.design.widget;

import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.view.MenuItem;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class NavigationViewItemSelectionsOnSubscribe implements Observable.OnSubscribe<MenuItem> {
  final NavigationView view;

  NavigationViewItemSelectionsOnSubscribe(NavigationView view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super MenuItem> subscriber) {
    verifyMainThread();

    NavigationView.OnNavigationItemSelectedListener listener =
        new NavigationView.OnNavigationItemSelectedListener() {
          @Override public boolean onNavigationItemSelected(MenuItem menuItem) {
            if (!subscriber.isUnsubscribed()) {
              subscriber.onNext(menuItem);
            }
            return true;
          }
        };
    view.setNavigationItemSelectedListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setNavigationItemSelectedListener(null);
      }
    });

    // Emit initial checked item, if one can be found.
    Menu menu = view.getMenu();
    for (int i = 0, count = menu.size(); i < count; i++) {
      MenuItem item = menu.getItem(i);
      if (item.isChecked()) {
        subscriber.onNext(item);
        break;
      }
    }
  }
}
