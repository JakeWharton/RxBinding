package com.jakewharton.rxbinding2.support.v4.view;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

final class ViewPagerPageScrolledObservable extends Observable<ViewPagerPageScrollEvent> {
  private final ViewPager view;

  ViewPagerPageScrolledObservable(ViewPager view) {
    this.view = view;
  }

  @Override
  protected void subscribeActual(Observer<? super ViewPagerPageScrollEvent> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.addOnPageChangeListener(listener);
  }

  static final class Listener extends MainThreadDisposable implements OnPageChangeListener {
    private final ViewPager view;
    private final Observer<? super ViewPagerPageScrollEvent> observer;

    Listener(ViewPager view, Observer<? super ViewPagerPageScrollEvent> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
      if (!isDisposed()) {
        ViewPagerPageScrollEvent event =
            ViewPagerPageScrollEvent.create(view, position, positionOffset,
                positionOffsetPixels);
        observer.onNext(event);
      }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDispose() {
      view.removeOnPageChangeListener(this);
    }
  }
}
