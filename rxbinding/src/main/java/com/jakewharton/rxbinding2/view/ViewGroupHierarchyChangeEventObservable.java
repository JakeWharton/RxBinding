package com.jakewharton.rxbinding2.view;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.OnHierarchyChangeListener;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class ViewGroupHierarchyChangeEventObservable
    extends Observable<ViewGroupHierarchyChangeEvent> {
  private final ViewGroup viewGroup;

  ViewGroupHierarchyChangeEventObservable(ViewGroup viewGroup) {
    this.viewGroup = viewGroup;
  }

  @Override
  protected void subscribeActual(Observer<? super ViewGroupHierarchyChangeEvent> observer) {
    verifyMainThread();
    Listener listener = new Listener(viewGroup, observer);
    observer.onSubscribe(listener);
    viewGroup.setOnHierarchyChangeListener(listener);
  }

  static final class Listener extends MainThreadDisposable implements OnHierarchyChangeListener {
    private final ViewGroup viewGroup;
    private final Observer<? super ViewGroupHierarchyChangeEvent> observer;

    Listener(ViewGroup viewGroup, Observer<? super ViewGroupHierarchyChangeEvent> observer) {
      this.viewGroup = viewGroup;
      this.observer = observer;
    }

    @Override public void onChildViewAdded(View parent, View child) {
      if (!isDisposed()) {
        observer.onNext(ViewGroupHierarchyChildViewAddEvent.create(viewGroup, child));
      }
    }

    @Override public void onChildViewRemoved(View parent, View child) {
      if (!isDisposed()) {
        observer.onNext(ViewGroupHierarchyChildViewRemoveEvent.create(viewGroup, child));
      }
    }

    @Override protected void onDispose() {
      viewGroup.setOnHierarchyChangeListener(null);
    }
  }
}
