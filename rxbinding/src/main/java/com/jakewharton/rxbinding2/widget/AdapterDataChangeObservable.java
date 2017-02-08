package com.jakewharton.rxbinding2.widget;

import android.database.DataSetObserver;
import android.widget.Adapter;
import com.jakewharton.rxbinding2.InitialValueObservable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

final class AdapterDataChangeObservable<T extends Adapter> extends InitialValueObservable<T> {
  private final T adapter;

  AdapterDataChangeObservable(T adapter) {
    this.adapter = adapter;
  }

  @Override protected void subscribeListener(Observer<? super T> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    ObserverDisposable<T> disposableDataSetObserver = new ObserverDisposable<>(adapter, observer);
    adapter.registerDataSetObserver(disposableDataSetObserver.dataSetObserver);
    observer.onSubscribe(disposableDataSetObserver);
  }

  @Override protected T getInitialValue() {
    return adapter;
  }

  static final class ObserverDisposable<T extends Adapter> extends MainThreadDisposable {
    private final T adapter;
    private final DataSetObserver dataSetObserver;

    ObserverDisposable(final T adapter, final Observer<? super T> observer) {
      this.adapter = adapter;
      this.dataSetObserver = new DataSetObserver() {
        @Override public void onChanged() {
          if (!isDisposed()) {
            observer.onNext(adapter);
          }
        }
      };
    }

    @Override protected void onDispose() {
      adapter.unregisterDataSetObserver(dataSetObserver);
    }
  }
}
