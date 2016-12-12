package com.jakewharton.rxbinding2.view;

import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewTreeObserver.OnDrawListener;
import com.jakewharton.rxbinding2.internal.Notification;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static android.os.Build.VERSION_CODES.JELLY_BEAN;
import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

@RequiresApi(JELLY_BEAN)
final class ViewTreeObserverDrawObservable extends Observable<Object> {
  private final View view;

  ViewTreeObserverDrawObservable(View view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super Object> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.getViewTreeObserver().addOnDrawListener(listener);
  }

  static final class Listener extends MainThreadDisposable implements OnDrawListener {
    private final View view;
    private final Observer<? super Object> observer;

    Listener(View view, Observer<? super Object> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override public void onDraw() {
      if (!isDisposed()) {
        observer.onNext(Notification.INSTANCE);
      }
    }

    @Override protected void onDispose() {
      view.getViewTreeObserver().removeOnDrawListener(this);
    }
  }
}
