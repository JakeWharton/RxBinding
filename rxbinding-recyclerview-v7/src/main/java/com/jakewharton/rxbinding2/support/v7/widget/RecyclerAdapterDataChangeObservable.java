package com.jakewharton.rxbinding2.support.v7.widget;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.support.v7.widget.RecyclerView.ViewHolder;
import com.jakewharton.rxbinding2.InitialValueObservable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

final class RecyclerAdapterDataChangeObservable<T extends Adapter<? extends ViewHolder>>
    extends InitialValueObservable<RecyclerAdapterDataEvent<T>> {
  private final T adapter;

  RecyclerAdapterDataChangeObservable(T adapter) {
    this.adapter = adapter;
  }

  @Override
  protected void subscribeListener(Observer<? super RecyclerAdapterDataEvent<T>> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(adapter, observer);
    observer.onSubscribe(listener);
    adapter.registerAdapterDataObserver(listener.dataObserver);
  }

  @Override protected RecyclerAdapterDataEvent<T> getInitialValue() {
    return RecyclerAdapterDataChangeEvent.create(adapter);
  }

  final class Listener extends MainThreadDisposable {
    private final T recyclerAdapter;
    final AdapterDataObserver dataObserver;

    Listener(final T recyclerAdapter,
        final Observer<? super RecyclerAdapterDataEvent<T>> observer) {
      this.recyclerAdapter = recyclerAdapter;
      this.dataObserver = new AdapterDataObserver() {
        @Override public void onItemRangeChanged(int positionStart, int itemCount) {
          onItemRangeChanged(positionStart, itemCount, null);
        }

        @Override public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
          if (!isDisposed()) {
            observer.onNext(
                RecyclerAdapterDataRangeChangeEvent.create(recyclerAdapter, positionStart,
                    itemCount, payload));
          }
        }

        @Override public void onItemRangeInserted(int positionStart, int itemCount) {
          if (!isDisposed()) {
            observer.onNext(
                RecyclerAdapterDataRangeInsertionEvent.create(recyclerAdapter, positionStart,
                    itemCount));
          }
        }

        @Override public void onItemRangeRemoved(int positionStart, int itemCount) {
          if (!isDisposed()) {
            observer.onNext(
                RecyclerAdapterDataRangeRemovalEvent.create(recyclerAdapter, positionStart,
                    itemCount));
          }
        }

        @Override public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
          if (!isDisposed()) {
            observer.onNext(
                RecyclerAdapterDataRangeMoveEvent.create(recyclerAdapter, fromPosition, toPosition,
                    itemCount));
          }
        }

        @Override public void onChanged() {
          if (!isDisposed()) {
            observer.onNext(RecyclerAdapterDataChangeEvent.create(recyclerAdapter));
          }
        }
      };
    }

    @Override protected void onDispose() {
      recyclerAdapter.unregisterAdapterDataObserver(dataObserver);
    }
  }
}
