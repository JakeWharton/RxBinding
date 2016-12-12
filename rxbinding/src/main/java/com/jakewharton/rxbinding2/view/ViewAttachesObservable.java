package com.jakewharton.rxbinding2.view;

import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import com.jakewharton.rxbinding2.internal.Notification;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class ViewAttachesObservable extends Observable<Object> {
  private final boolean callOnAttach;
  private final View view;

  ViewAttachesObservable(View view, boolean callOnAttach) {
    this.view = view;
    this.callOnAttach = callOnAttach;
  }

  @Override protected void subscribeActual(final Observer<? super Object> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, callOnAttach, observer);
    observer.onSubscribe(listener);
    view.addOnAttachStateChangeListener(listener);
  }

  static final class Listener extends MainThreadDisposable implements OnAttachStateChangeListener {
    private final View view;
    private final boolean callOnAttach;
    private final Observer<? super Object> observer;

    Listener(View view, boolean callOnAttach, Observer<? super Object> observer) {
      this.view = view;
      this.callOnAttach = callOnAttach;
      this.observer = observer;
    }

    @Override public void onViewAttachedToWindow(View v) {
      if (callOnAttach && !isDisposed()) {
        observer.onNext(Notification.INSTANCE);
      }
    }

    @Override public void onViewDetachedFromWindow(View v) {
      if (!callOnAttach && !isDisposed()) {
        observer.onNext(Notification.INSTANCE);
      }
    }

    @Override protected void onDispose() {
      view.removeOnAttachStateChangeListener(this);
    }
  }
}
