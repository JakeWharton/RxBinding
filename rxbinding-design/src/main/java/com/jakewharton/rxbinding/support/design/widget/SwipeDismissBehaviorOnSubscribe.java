package com.jakewharton.rxbinding.support.design.widget;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.SwipeDismissBehavior;
import android.view.View;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import rx.Observable;
import rx.Subscriber;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class SwipeDismissBehaviorOnSubscribe implements Observable.OnSubscribe<View> {
  private final View view;

  public SwipeDismissBehaviorOnSubscribe(View view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super View> subscriber) {
    checkUiThread();

    SwipeDismissBehavior.OnDismissListener listener = new SwipeDismissBehavior.OnDismissListener() {
      @Override public void onDismiss(View view) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(view);
        }
      }

      @Override public void onDragStateChanged(int i) {
      }
    };

    if (!(view.getLayoutParams() instanceof CoordinatorLayout.LayoutParams)) {
      throw new IllegalArgumentException("The view is not in a Coordinator Layout.");
    }
    final SwipeDismissBehavior behavior =
        (SwipeDismissBehavior) ((CoordinatorLayout.LayoutParams) view.getLayoutParams()).getBehavior();
    if (behavior == null) {
      throw new IllegalStateException("There's no behavior set on this view.");
    }
    behavior.setListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        behavior.setListener(null);
      }
    });
  }
}
