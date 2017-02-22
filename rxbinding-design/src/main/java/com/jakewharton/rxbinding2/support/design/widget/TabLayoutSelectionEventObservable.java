package com.jakewharton.rxbinding2.support.design.widget;

import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

final class TabLayoutSelectionEventObservable extends Observable<TabLayoutSelectionEvent> {
  private final TabLayout view;

  TabLayoutSelectionEventObservable(TabLayout view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super TabLayoutSelectionEvent> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.addOnTabSelectedListener(listener);

    int index = view.getSelectedTabPosition();
    if (index != -1) {
      observer.onNext(TabLayoutSelectionSelectedEvent.create(view, view.getTabAt(index)));
    }
  }

  final class Listener extends MainThreadDisposable implements TabLayout.OnTabSelectedListener {
    private final TabLayout tabLayout;
    private final Observer<? super TabLayoutSelectionEvent> observer;

    Listener(TabLayout tabLayout, Observer<? super TabLayoutSelectionEvent> observer) {
      this.tabLayout = tabLayout;
      this.observer = observer;
    }

    @Override public void onTabSelected(Tab tab) {
      if (!isDisposed()) {
        observer.onNext(TabLayoutSelectionSelectedEvent.create(view, tab));
      }
    }

    @Override public void onTabUnselected(Tab tab) {
      if (!isDisposed()) {
        observer.onNext(TabLayoutSelectionUnselectedEvent.create(view, tab));
      }
    }

    @Override public void onTabReselected(Tab tab) {
      if (!isDisposed()) {
        observer.onNext(TabLayoutSelectionReselectedEvent.create(view, tab));
      }
    }

    @Override protected void onDispose() {
      tabLayout.removeOnTabSelectedListener(this);
    }
  }
}
