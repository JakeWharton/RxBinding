package com.jakewharton.rxbinding2.widget;

import android.view.View;
import android.widget.AdapterView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static android.widget.AdapterView.INVALID_POSITION;
import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class AdapterViewItemSelectionObservable extends Observable<Integer> {
  private final AdapterView<?> view;

  AdapterViewItemSelectionObservable(AdapterView<?> view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super Integer> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    view.setOnItemSelectedListener(listener);
    observer.onSubscribe(listener);
    observer.onNext(view.getSelectedItemPosition());
  }

  static final class Listener extends MainThreadDisposable
          implements AdapterView.OnItemSelectedListener {

    private final AdapterView<?> view;
    private final Observer<? super Integer> observer;

    Listener(AdapterView<?> view, Observer<? super Integer> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override public void onItemSelected(AdapterView<?> adapterView, View view,
                                         int position, long id) {
      if (!isDisposed()) {
        observer.onNext(position);
      }
    }

    @Override public void onNothingSelected(AdapterView<?> adapterView) {
      if (!isDisposed()) {
        observer.onNext(INVALID_POSITION);
      }
    }

    @Override protected void onDispose() {
      view.setOnItemSelectedListener(null);
    }
  }
}
