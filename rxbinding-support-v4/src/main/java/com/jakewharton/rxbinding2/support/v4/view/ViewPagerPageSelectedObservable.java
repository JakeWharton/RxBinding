package com.jakewharton.rxbinding2.support.v4.view;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.*;

final class ViewPagerPageSelectedObservable extends Observable<Integer> {
  private final ViewPager view;

  ViewPagerPageSelectedObservable(ViewPager view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super Integer> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.addOnPageChangeListener(listener);
    observer.onNext(view.getCurrentItem());
  }

  static final class Listener extends MainThreadDisposable implements OnPageChangeListener {
    private final ViewPager view;
    private final Observer<? super Integer> observer;

    Listener(ViewPager view, Observer<? super Integer> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override public void onPageSelected(int position) {
      if (!isDisposed()) {
        observer.onNext(position);
      }
    }

    @Override public void onPageScrollStateChanged(int state) {

    }

    @Override protected void onDispose() {
      view.removeOnPageChangeListener(this);
    }
  }
}
