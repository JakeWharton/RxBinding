package com.jakewharton.rxbinding2.view;

import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding2.view.ViewAttachEvent.Kind.ATTACH;
import static com.jakewharton.rxbinding2.view.ViewAttachEvent.Kind.DETACH;
import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class ViewAttachEventObservable extends Observable<ViewAttachEvent> {
  private final View view;

  ViewAttachEventObservable(View view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super ViewAttachEvent> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.addOnAttachStateChangeListener(listener);
  }

  static final class Listener extends MainThreadDisposable implements OnAttachStateChangeListener {
    private final View view;
    private final Observer<? super ViewAttachEvent> observer;

    Listener(View view, Observer<? super ViewAttachEvent> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override public void onViewAttachedToWindow(View v) {
      if (!isDisposed()) {
        observer.onNext(ViewAttachEvent.create(view, ATTACH));
      }
    }

    @Override public void onViewDetachedFromWindow(View v) {
      if (!isDisposed()) {
        observer.onNext(ViewAttachEvent.create(view, DETACH));
      }
    }

    @Override protected void onDispose() {
      view.removeOnAttachStateChangeListener(this);
    }
  }
}
