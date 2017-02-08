package com.jakewharton.rxbinding2.support.v7.widget;

import android.support.v7.widget.PopupMenu;
import com.jakewharton.rxbinding2.internal.Notification;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

final class PopupMenuDismissObservable extends Observable<Object> {
  private final PopupMenu view;

  PopupMenuDismissObservable(PopupMenu view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super Object> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.setOnDismissListener(listener);
  }

  static final class Listener extends MainThreadDisposable implements PopupMenu.OnDismissListener {
    private final PopupMenu popupMenu;
    private final Observer<? super Object> observer;

    Listener(PopupMenu popupMenu, Observer<? super Object> observer) {
      this.popupMenu = popupMenu;
      this.observer = observer;
    }

    @Override public void onDismiss(PopupMenu menu) {
      if (!isDisposed()) {
        observer.onNext(Notification.INSTANCE);
      }
    }

    @Override protected void onDispose() {
      popupMenu.setOnDismissListener(null);
    }
  }
}
