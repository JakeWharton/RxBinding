package com.jakewharton.rxbinding2.view;

import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;
import com.jakewharton.rxbinding2.internal.Notification;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;
import java.util.concurrent.Callable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class ViewTreeObserverPreDrawObservable extends Observable<Object> {
  private final View view;
  private final Callable<Boolean> proceedDrawingPass;

  ViewTreeObserverPreDrawObservable(View view, Callable<Boolean> proceedDrawingPass) {
    this.view = view;
    this.proceedDrawingPass = proceedDrawingPass;
  }

  @Override protected void subscribeActual(Observer<? super Object> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, proceedDrawingPass, observer);
    observer.onSubscribe(listener);
    view.getViewTreeObserver().addOnPreDrawListener(listener);
  }

  static final class Listener extends MainThreadDisposable implements OnPreDrawListener {
    private final View view;
    private final Callable<Boolean> proceedDrawingPass;
    private final Observer<? super Object> observer;

    Listener(View view, Callable<Boolean> proceedDrawingPass, Observer<? super Object> observer) {
      this.view = view;
      this.proceedDrawingPass = proceedDrawingPass;
      this.observer = observer;
    }

    @Override public boolean onPreDraw() {
      if (!isDisposed()) {
        observer.onNext(Notification.INSTANCE);
        try {
          return proceedDrawingPass.call();
        } catch (Exception e) {
          observer.onError(e);
          dispose();
        }
      }
      return true;
    }

    @Override protected void onDispose() {
      view.getViewTreeObserver().removeOnPreDrawListener(this);
    }
  }
}
