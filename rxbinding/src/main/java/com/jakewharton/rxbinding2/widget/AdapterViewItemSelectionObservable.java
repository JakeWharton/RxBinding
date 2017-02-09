package com.jakewharton.rxbinding2.widget;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import com.jakewharton.rxbinding2.InitialValueObservable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static android.widget.AdapterView.INVALID_POSITION;
import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

final class AdapterViewItemSelectionObservable extends InitialValueObservable<Integer> {
  private final AdapterView<?> view;

  AdapterViewItemSelectionObservable(AdapterView<?> view) {
    this.view = view;
  }

  @Override protected void subscribeListener(Observer<? super Integer> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(view, observer);
    view.setOnItemSelectedListener(listener);
    observer.onSubscribe(listener);
  }

  @Override protected Integer getInitialValue() {
    return view.getSelectedItemPosition();
  }

  static final class Listener extends MainThreadDisposable implements OnItemSelectedListener {
    private final AdapterView<?> view;
    private final Observer<? super Integer> observer;

    Listener(AdapterView<?> view, Observer<? super Integer> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
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
