package com.jakewharton.rxbinding2.support.v7.widget;

import android.support.v7.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

final class RecyclerViewScrollStateChangeObservable extends Observable<Integer> {
  private final RecyclerView view;

  RecyclerViewScrollStateChangeObservable(RecyclerView view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super Integer> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.addOnScrollListener(listener.scrollListener);
  }

  final class Listener extends MainThreadDisposable {
    private final RecyclerView recyclerView;
    private final RecyclerView.OnScrollListener scrollListener;

    Listener(RecyclerView recyclerView, final Observer<? super Integer> observer) {
      this.recyclerView = recyclerView;
      this.scrollListener = new RecyclerView.OnScrollListener() {
        @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
          if (!isDisposed()) {
            observer.onNext(newState);
          }
        }
      };
    }

    @Override protected void onDispose() {
      recyclerView.removeOnScrollListener(scrollListener);
    }
  }
}
