package com.jakewharton.rxbinding2.support.v7.widget;

import android.support.v7.widget.RecyclerView;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

public class RecyclerViewFlingObservable extends Observable<RecyclerViewFlingEvent> {
  private final RecyclerView view;

  RecyclerViewFlingObservable(RecyclerView view) {
    this.view = view;
  }

  @Override
  protected void subscribeActual(Observer<? super RecyclerViewFlingEvent> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.setOnFlingListener(listener.flingListener);
  }

  final class Listener extends MainThreadDisposable {
    private final RecyclerView recyclerView;
    final RecyclerView.OnFlingListener flingListener;

    Listener(final RecyclerView recyclerView,
             final Observer<? super RecyclerViewFlingEvent> observer) {
      this.recyclerView = recyclerView;
      this.flingListener = new RecyclerView.OnFlingListener() {
        @Override
        public boolean onFling(int velocityX, int velocityY) {
          if (!isDisposed()) {
            observer.onNext(RecyclerViewFlingEvent.create(recyclerView, velocityX, velocityY));
          }
          return false;
        }
      };
    }

    @Override
    protected void onDispose() {
      recyclerView.setOnFlingListener(null);
    }
  }
}
