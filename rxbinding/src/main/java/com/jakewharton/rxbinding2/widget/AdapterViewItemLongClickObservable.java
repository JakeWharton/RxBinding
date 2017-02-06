package com.jakewharton.rxbinding2.widget;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;
import java.util.concurrent.Callable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

final class AdapterViewItemLongClickObservable extends Observable<Integer> {
  private final AdapterView<?> view;
  private final Callable<Boolean> handled;

  AdapterViewItemLongClickObservable(AdapterView<?> view, Callable<Boolean> handled) {
    this.view = view;
    this.handled = handled;
  }

  @Override protected void subscribeActual(Observer<? super Integer> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(view, observer, handled);
    observer.onSubscribe(listener);
    view.setOnItemLongClickListener(listener);
  }

  static final class Listener extends MainThreadDisposable implements OnItemLongClickListener {
    private final AdapterView<?> view;
    private final Observer<? super Integer> observer;
    private final Callable<Boolean> handled;

    Listener(AdapterView<?> view, Observer<? super Integer> observer,
             Callable<Boolean> handled) {
      this.view = view;
      this.observer = observer;
      this.handled = handled;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
      if (!isDisposed()) {
        try {
          if (handled.call()) {
            observer.onNext(position);
            return true;
          }
        } catch (Exception e) {
          observer.onError(e);
          dispose();
        }
      }
      return false;
    }

    @Override protected void onDispose() {
      view.setOnItemLongClickListener(null);
    }
  }
}
