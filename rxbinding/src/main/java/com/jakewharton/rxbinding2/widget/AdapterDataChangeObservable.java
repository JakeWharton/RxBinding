package com.jakewharton.rxbinding2.widget;

import android.database.DataSetObserver;
import android.widget.Adapter;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class AdapterDataChangeObservable<T extends Adapter> extends Observable<T> {
  private final T adapter;

  AdapterDataChangeObservable(T adapter) {
    this.adapter = adapter;
  }

  @Override protected void subscribeActual(Observer<? super T> observer) {
    verifyMainThread();
    MainThreadDisposableDataSetObserver<T> disposableDataSetObserver =
            new MainThreadDisposableDataSetObserver<>(adapter, observer);
    adapter.registerDataSetObserver(disposableDataSetObserver.dataSetObserver);
    observer.onSubscribe(disposableDataSetObserver);
    observer.onNext(adapter);
  }

  static final class MainThreadDisposableDataSetObserver<T extends Adapter>
          extends MainThreadDisposable {

    private final T adapter;
    private final DataSetObserver dataSetObserver;

    MainThreadDisposableDataSetObserver(final T adapter, final Observer<? super T> observer) {
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
