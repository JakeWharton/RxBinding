package com.jakewharton.rxbinding2.widget;

import android.widget.PopupMenu;
import com.jakewharton.rxbinding2.internal.Notification;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class PopupMenuDismissObservable extends Observable<Notification> {
  private final PopupMenu view;

  PopupMenuDismissObservable(PopupMenu view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super Notification> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    view.setOnDismissListener(listener);
    observer.onSubscribe(listener);
  }

  static final class Listener extends MainThreadDisposable implements PopupMenu.OnDismissListener {
    private final PopupMenu view;
    private final Observer<? super Notification> observer;

    Listener(PopupMenu view, Observer<? super Notification> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override public void onDismiss(PopupMenu popupMenu) {
      if (!isDisposed()) {
        observer.onNext(Notification.INSTANCE);
      }
    }

    @Override protected void onDispose() {
      view.setOnDismissListener(null);
    }
  }
}
