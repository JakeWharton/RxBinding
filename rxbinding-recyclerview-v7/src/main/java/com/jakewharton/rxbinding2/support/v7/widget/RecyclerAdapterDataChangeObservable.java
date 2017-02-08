package com.jakewharton.rxbinding2.support.v7.widget;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.support.v7.widget.RecyclerView.ViewHolder;
import com.jakewharton.rxbinding2.InitialValueObservable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

final class RecyclerAdapterDataChangeObservable<T extends Adapter<? extends ViewHolder>>
    extends InitialValueObservable<T> {
  private final T adapter;

  RecyclerAdapterDataChangeObservable(T adapter) {
    this.adapter = adapter;
  }

  @Override protected void subscribeListener(Observer<? super T> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(adapter, observer);
    observer.onSubscribe(listener);
    adapter.registerAdapterDataObserver(listener.dataObserver);
  }

  @Override protected T getInitialValue() {
    return adapter;
  }

  final class Listener extends MainThreadDisposable {
    private final T recyclerAdapter;
    private final AdapterDataObserver dataObserver;

    Listener(final T recyclerAdapter, final Observer<? super T> observer) {
      this.recyclerAdapter = recyclerAdapter;
      this.dataObserver = new AdapterDataObserver() {
        @Override public void onChanged() {
          if (!isDisposed()) {
            observer.onNext(recyclerAdapter);
          }
        }
      };
    }

    @Override protected void onDispose() {
      recyclerAdapter.unregisterAdapterDataObserver(dataObserver);
    }
  }
}
