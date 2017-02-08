package com.jakewharton.rxbinding2.support.design.widget;

import android.support.design.widget.CoordinatorLayout.LayoutParams;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.design.widget.SwipeDismissBehavior.OnDismissListener;
import android.view.View;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

final class SwipeDismissBehaviorObservable extends Observable<View> {
  private final View view;

  SwipeDismissBehaviorObservable(View view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super View> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    if (!(view.getLayoutParams() instanceof LayoutParams)) {
      throw new IllegalArgumentException("The view is not in a Coordinator Layout.");
    }
    LayoutParams params = (LayoutParams) view.getLayoutParams();
    final SwipeDismissBehavior behavior = (SwipeDismissBehavior) params.getBehavior();
    if (behavior == null) {
      throw new IllegalStateException("There's no behavior set on this view.");
    }
    Listener listener = new Listener(behavior, observer);
    observer.onSubscribe(listener);
    behavior.setListener(listener);
  }

  static final class Listener extends MainThreadDisposable implements OnDismissListener {
    private final SwipeDismissBehavior swipeDismissBehavior;
    private final Observer<? super View> observer;

    Listener(SwipeDismissBehavior swipeDismissBehavior, Observer<? super View> observer) {
      this.swipeDismissBehavior = swipeDismissBehavior;
      this.observer = observer;
    }

    @Override public void onDismiss(View view) {
      if (!isDisposed()) {
        observer.onNext(view);
      }
    }

    @Override public void onDragStateChanged(int state) {
    }

    @Override protected void onDispose() {
      swipeDismissBehavior.setListener(null);
    }
  }
}
