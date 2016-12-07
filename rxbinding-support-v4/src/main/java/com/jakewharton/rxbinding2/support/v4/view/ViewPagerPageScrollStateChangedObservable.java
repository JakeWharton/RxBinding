package com.jakewharton.rxbinding2.support.v4.view;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class ViewPagerPageScrollStateChangedObservable extends Observable<Integer> {
  private final ViewPager view;

  ViewPagerPageScrollStateChangedObservable(ViewPager view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super Integer> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.addOnPageChangeListener(listener);
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

    }

    @Override public void onPageScrollStateChanged(int state) {
      if (!isDisposed()) {
        observer.onNext(state);
      }
    }

    @Override protected void onDispose() {
      if (!isDisposed()) {
        view.removeOnPageChangeListener(this);
      }
    }
  }
}
