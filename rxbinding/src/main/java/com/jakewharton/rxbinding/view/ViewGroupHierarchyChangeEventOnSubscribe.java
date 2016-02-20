package com.jakewharton.rxbinding.view;

import android.view.View;
import android.view.ViewGroup;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class ViewGroupHierarchyChangeEventOnSubscribe
    implements Observable.OnSubscribe<ViewGroupHierarchyChangeEvent> {
  final ViewGroup viewGroup;

  ViewGroupHierarchyChangeEventOnSubscribe(ViewGroup viewGroup) {
    this.viewGroup = viewGroup;
  }

  @Override public void call(final Subscriber<? super ViewGroupHierarchyChangeEvent> subscriber) {
    verifyMainThread();

    ViewGroup.OnHierarchyChangeListener listener = new ViewGroup.OnHierarchyChangeListener() {
      @Override public void onChildViewAdded(View parent, View child) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(
            ViewGroupHierarchyChildViewAddEvent.create(((ViewGroup) parent), child));
        }
      }

      @Override public void onChildViewRemoved(View parent, View child) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(
            ViewGroupHierarchyChildViewRemoveEvent.create(((ViewGroup) parent), child));
        }
      }
    };

    viewGroup.setOnHierarchyChangeListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        viewGroup.setOnHierarchyChangeListener(null);
      }
    });
  }
}
