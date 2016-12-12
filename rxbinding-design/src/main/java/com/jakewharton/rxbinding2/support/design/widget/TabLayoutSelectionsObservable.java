package com.jakewharton.rxbinding2.support.design.widget;

import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class TabLayoutSelectionsObservable extends Observable<Tab> {
  private final TabLayout view;

  TabLayoutSelectionsObservable(TabLayout view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super Tab> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.addOnTabSelectedListener(listener);
    int index = view.getSelectedTabPosition();
    if (index != -1) {
      observer.onNext(view.getTabAt(index));
    }
  }

  static final class Listener extends MainThreadDisposable implements TabLayout.OnTabSelectedListener {
    private final TabLayout tabLayout;
    private final Observer<? super Tab> observer;

    Listener(TabLayout tabLayout, Observer<? super Tab> observer) {
      this.tabLayout = tabLayout;
      this.observer = observer;
    }

    @Override protected void onDispose() {
      tabLayout.removeOnTabSelectedListener(this);
    }

    @Override public void onTabSelected(Tab tab) {
      if (!isDisposed()) {
        observer.onNext(tab);
      }
    }

    @Override public void onTabUnselected(Tab tab) {
    }

    @Override public void onTabReselected(Tab tab) {
    }
  }
}
