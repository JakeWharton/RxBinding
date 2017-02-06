package com.jakewharton.rxbinding2.widget;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

final class AdapterViewItemClickEventObservable extends Observable<AdapterViewItemClickEvent> {
  private final AdapterView<?> view;

  AdapterViewItemClickEventObservable(AdapterView<?> view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super AdapterViewItemClickEvent> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.setOnItemClickListener(listener);
  }

  static final class Listener extends MainThreadDisposable implements OnItemClickListener {
    private final AdapterView<?> view;
    private final Observer<? super AdapterViewItemClickEvent> observer;

    Listener(AdapterView<?> view, Observer<? super AdapterViewItemClickEvent> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      if (!isDisposed()) {
        observer.onNext(AdapterViewItemClickEvent.create(parent, view, position, id));
      }
    }

    @Override protected void onDispose() {
      view.setOnItemClickListener(null);
    }
  }
}
