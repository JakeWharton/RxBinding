package com.jakewharton.rxbinding2.widget;

import android.view.View;
import android.widget.AdapterView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static android.widget.AdapterView.INVALID_POSITION;
import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class AdapterViewSelectionObservable extends Observable<AdapterViewSelectionEvent> {
  private final AdapterView<?> view;

  AdapterViewSelectionObservable(AdapterView<?> view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super AdapterViewSelectionEvent> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    view.setOnItemSelectedListener(listener);
    observer.onSubscribe(listener);
    observer.onNext(createInitialEvent());
  }

  private AdapterViewSelectionEvent createInitialEvent() {
    int selectedPosition = view.getSelectedItemPosition();
    if (selectedPosition == INVALID_POSITION) {
      return AdapterViewNothingSelectionEvent.create(view);
    } else {
      View selectedView = view.getSelectedView();
      long selectedId = view.getSelectedItemId();
      return AdapterViewItemSelectionEvent.create(view, selectedView, selectedPosition, selectedId);
    }
  }

  static final class Listener extends MainThreadDisposable
          implements AdapterView.OnItemSelectedListener {

    private final AdapterView<?> view;
    private final Observer<? super AdapterViewSelectionEvent> observer;

    Listener(AdapterView<?> view, Observer<? super AdapterViewSelectionEvent> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      if (!isDisposed()) {
        observer.onNext(AdapterViewItemSelectionEvent.create(parent, view, position, id));
      }
    }

    @Override public void onNothingSelected(AdapterView<?> parent) {
      if (!isDisposed()) {
        observer.onNext(AdapterViewNothingSelectionEvent.create(parent));
      }
    }

    @Override protected void onDispose() {
      view.setOnItemSelectedListener(null);
    }
  }
}
