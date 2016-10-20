package com.jakewharton.rxbinding.support.design.widget;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class BottomNavigationViewItemSelectionsOnSubscribe
    implements Observable.OnSubscribe<MenuItem> {
  final BottomNavigationView view;

  BottomNavigationViewItemSelectionsOnSubscribe(BottomNavigationView view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super MenuItem> subscriber) {
    verifyMainThread();

    final BottomNavigationView.OnNavigationItemSelectedListener listener =
        new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (!subscriber.isUnsubscribed()) {
              subscriber.onNext(item);
            }
            return true;
          }
        };

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnNavigationItemSelectedListener(null);
      }
    });

    view.setOnNavigationItemSelectedListener(listener);

    // Emit initial item, if one can be found
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
