package com.jakewharton.rxbinding2.support.v7.widget;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnChildAttachStateChangeListener;
import android.view.View;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class RecyclerViewChildAttachStateChangeEventObservable extends Observable<RecyclerViewChildAttachStateChangeEvent> {
  private final RecyclerView view;

  RecyclerViewChildAttachStateChangeEventObservable(RecyclerView recyclerView) {
    this.view = recyclerView;
  }

  @Override protected void subscribeActual(Observer<? super RecyclerViewChildAttachStateChangeEvent> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.addOnChildAttachStateChangeListener(listener);
  }

  final class Listener extends MainThreadDisposable implements OnChildAttachStateChangeListener {
    private final RecyclerView recyclerView;
    private final Observer<? super RecyclerViewChildAttachStateChangeEvent> observer;

    Listener(RecyclerView recyclerView, Observer<? super RecyclerViewChildAttachStateChangeEvent> observer) {
      this.recyclerView = recyclerView;
      this.observer = observer;
    }

    @Override public void onChildViewAttachedToWindow(View childView) {
      if (!isDisposed()) {
        observer.onNext(RecyclerViewChildAttachEvent.create(recyclerView, childView));
      }
    }

    @Override public void onChildViewDetachedFromWindow(View childView) {
      if (!isDisposed()) {
        observer.onNext(RecyclerViewChildDetachEvent.create(recyclerView, childView));
      }
    }

    @Override protected void onDispose() {
      recyclerView.removeOnChildAttachStateChangeListener(this);
    }
  }
}
