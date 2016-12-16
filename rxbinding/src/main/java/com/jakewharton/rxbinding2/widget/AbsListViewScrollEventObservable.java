package com.jakewharton.rxbinding2.widget;

import android.widget.AbsListView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class AbsListViewScrollEventObservable extends Observable<AbsListViewScrollEvent> {
  private final AbsListView view;

  AbsListViewScrollEventObservable(AbsListView view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super AbsListViewScrollEvent> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.setOnScrollListener(listener);
  }

  static final class Listener extends MainThreadDisposable implements AbsListView.OnScrollListener {
    private final AbsListView view;
    private final Observer<? super AbsListViewScrollEvent> observer;
    private int currentScrollState = SCROLL_STATE_IDLE;

    Listener(AbsListView view, Observer<? super AbsListViewScrollEvent> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override public void onScrollStateChanged(AbsListView absListView, int scrollState) {
      currentScrollState = scrollState;
      if (!isDisposed()) {
        AbsListViewScrollEvent event =
              AbsListViewScrollEvent.create(view, scrollState, view.getFirstVisiblePosition(),
                  view.getChildCount(), view.getCount());
        observer.onNext(event);
      }
    }

    @Override public void onScroll(AbsListView absListView, int firstVisibleItem,
                                   int visibleItemCount, int totalItemCount) {
      if (!isDisposed()) {
        AbsListViewScrollEvent event =
              AbsListViewScrollEvent.create(view, currentScrollState, firstVisibleItem,
                  visibleItemCount, totalItemCount);
        observer.onNext(event);
      }
    }

    @Override protected void onDispose() {
      view.setOnScrollListener(null);
    }
  }
}
