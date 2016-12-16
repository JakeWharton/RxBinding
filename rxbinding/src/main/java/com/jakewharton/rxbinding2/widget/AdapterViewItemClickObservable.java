package com.jakewharton.rxbinding2.widget;

import android.view.View;
import android.widget.AdapterView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class AdapterViewItemClickObservable extends Observable<Integer> {
  private final AdapterView<?> view;

  AdapterViewItemClickObservable(AdapterView<?> view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super Integer> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.setOnItemClickListener(listener);
  }

  static final class Listener extends MainThreadDisposable
          implements AdapterView.OnItemClickListener {

    private final AdapterView<?> view;
    private final Observer<? super Integer> observer;

    Listener(AdapterView<?> view, Observer<? super Integer> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override public void onItemClick(AdapterView<?> adapterView, View view,
                                      int position, long id) {
      if (!isDisposed()) {
        observer.onNext(position);
      }
    }

    @Override protected void onDispose() {
      view.setOnItemClickListener(null);
    }
  }
}
