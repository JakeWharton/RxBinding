package com.jakewharton.rxbinding2.widget;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import com.jakewharton.rxbinding2.InitialValueObservable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static android.widget.AdapterView.INVALID_POSITION;
import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

final class AdapterViewSelectionObservable
    extends InitialValueObservable<AdapterViewSelectionEvent> {
  private final AdapterView<?> view;

  AdapterViewSelectionObservable(AdapterView<?> view) {
    this.view = view;
  }

  @Override protected void subscribeListener(Observer<? super AdapterViewSelectionEvent> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(view, observer);
    view.setOnItemSelectedListener(listener);
    observer.onSubscribe(listener);
  }

  @Override protected AdapterViewSelectionEvent getInitialValue() {
    int selectedPosition = view.getSelectedItemPosition();
    if (selectedPosition == INVALID_POSITION) {
      return AdapterViewNothingSelectionEvent.create(view);
    } else {
      View selectedView = view.getSelectedView();
      long selectedId = view.getSelectedItemId();
      return AdapterViewItemSelectionEvent.create(view, selectedView, selectedPosition, selectedId);
    }
  }

  static final class Listener extends MainThreadDisposable implements OnItemSelectedListener {
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
