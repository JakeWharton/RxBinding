package com.jakewharton.rxbinding2.view;

import android.view.View;
import android.view.View.OnLongClickListener;
import com.jakewharton.rxbinding2.internal.Notification;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;
import java.util.concurrent.Callable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class ViewLongClickObservable extends Observable<Object> {
  private final View view;
  private final Callable<Boolean> handled;

  ViewLongClickObservable(View view, Callable<Boolean> handled) {
    this.view = view;
    this.handled = handled;
  }

  @Override protected void subscribeActual(Observer<? super Object> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, handled, observer);
    observer.onSubscribe(listener);
    view.setOnLongClickListener(listener);
  }

  static final class Listener extends MainThreadDisposable implements OnLongClickListener {
    private final View view;
    private final Observer<? super Object> observer;
    private final Callable<Boolean> handled;

    Listener(View view, Callable<Boolean> handled, Observer<? super Object> observer) {
      this.view = view;
      this.observer = observer;
      this.handled = handled;
    }

    @Override public boolean onLongClick(View v) {
      if (!isDisposed()) {
        try {
          if (handled.call()) {
            observer.onNext(Notification.INSTANCE);
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
      view.setOnLongClickListener(null);
    }
  }
}
