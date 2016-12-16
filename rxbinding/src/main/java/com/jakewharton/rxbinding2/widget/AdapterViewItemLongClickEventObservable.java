package com.jakewharton.rxbinding2.widget;

import android.view.View;
import android.widget.AdapterView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.functions.Predicate;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class AdapterViewItemLongClickEventObservable
        extends Observable<AdapterViewItemLongClickEvent> {

  private final AdapterView<?> view;
  private final Predicate<? super AdapterViewItemLongClickEvent> handled;

  AdapterViewItemLongClickEventObservable(
          AdapterView<?> view, Predicate<? super AdapterViewItemLongClickEvent> handled) {
    this.view = view;
    this.handled = handled;
  }

  @Override protected void subscribeActual(
          Observer<? super AdapterViewItemLongClickEvent> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer, handled);
    observer.onSubscribe(listener);
    view.setOnItemLongClickListener(listener);
  }

  static final class Listener extends MainThreadDisposable
          implements AdapterView.OnItemLongClickListener {

    private final AdapterView<?> view;
    private final Observer<? super AdapterViewItemLongClickEvent> observer;
    private final Predicate<? super AdapterViewItemLongClickEvent> handled;

    Listener(AdapterView<?> view, Observer<? super AdapterViewItemLongClickEvent> observer,
             Predicate<? super AdapterViewItemLongClickEvent> handled) {
      this.view = view;
      this.observer = observer;
      this.handled = handled;
    }

    @Override public boolean onItemLongClick(AdapterView<?> parent, View view,
                                             int position, long id) {
      if (!isDisposed()) {
        AdapterViewItemLongClickEvent event =
                AdapterViewItemLongClickEvent.create(parent, view, position, id);
        try {
          if (handled.test(event)) {
            observer.onNext(event);
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
