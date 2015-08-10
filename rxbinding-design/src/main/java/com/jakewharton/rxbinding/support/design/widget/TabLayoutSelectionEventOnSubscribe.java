package com.jakewharton.rxbinding.support.design.widget;

import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import rx.Observable;
import rx.Subscriber;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;
import static com.jakewharton.rxbinding.support.design.widget.TabLayoutSelectionEvent.Kind.RESELECTED;
import static com.jakewharton.rxbinding.support.design.widget.TabLayoutSelectionEvent.Kind.SELECTED;
import static com.jakewharton.rxbinding.support.design.widget.TabLayoutSelectionEvent.Kind.UNSELECTED;

final class TabLayoutSelectionEventOnSubscribe
    implements Observable.OnSubscribe<TabLayoutSelectionEvent> {
  private final TabLayout view;

  public TabLayoutSelectionEventOnSubscribe(TabLayout view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super TabLayoutSelectionEvent> subscriber) {
    checkUiThread();

    final TabLayout.OnTabSelectedListener listener = new TabLayout.OnTabSelectedListener() {
      @Override public void onTabSelected(Tab tab) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(TabLayoutSelectionEvent.create(view, SELECTED, tab));
        }
      }

      @Override public void onTabUnselected(Tab tab) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(TabLayoutSelectionEvent.create(view, UNSELECTED, tab));
        }
      }

      @Override public void onTabReselected(Tab tab) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(TabLayoutSelectionEvent.create(view, RESELECTED, tab));
        }
      }
    };
    view.setOnTabSelectedListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnTabSelectedListener(null);
      }
    });

    int index = view.getSelectedTabPosition();
    if (index != -1) {
      subscriber.onNext(TabLayoutSelectionEvent.create(view, SELECTED, view.getTabAt(index)));
    }
  }
}
