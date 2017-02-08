package com.jakewharton.rxbinding2.view;

import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import com.jakewharton.rxbinding2.internal.Notification;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

final class ViewTreeObserverGlobalLayoutObservable extends Observable<Object> {
  private final View view;

  ViewTreeObserverGlobalLayoutObservable(View view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super Object> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.getViewTreeObserver()
        .addOnGlobalLayoutListener(listener);
  }

  static final class Listener extends MainThreadDisposable implements OnGlobalLayoutListener {
    private final View view;
    private final Observer<? super Object> observer;

    Listener(View view, Observer<? super Object> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override public void onGlobalLayout() {
      if (!isDisposed()) {
        observer.onNext(Notification.INSTANCE);
      }
    }

    @Override protected void onDispose() {
      //noinspection deprecation Change when minSdkVersion = 16
      view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }
  }
}
