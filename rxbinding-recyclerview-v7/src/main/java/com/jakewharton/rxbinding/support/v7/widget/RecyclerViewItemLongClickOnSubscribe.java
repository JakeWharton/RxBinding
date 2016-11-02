package com.jakewharton.rxbinding.support.v7.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class RecyclerViewItemLongClickOnSubscribe implements Observable.OnSubscribe<Integer> {
  final RecyclerView recyclerView;

  public RecyclerViewItemLongClickOnSubscribe(RecyclerView recyclerView) {
    this.recyclerView = recyclerView;
  }

  @Override public void call(final Subscriber<? super Integer> subscriber) {
    verifyMainThread();

    final View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
      @Override public boolean onLongClick(View view) {
        if (!subscriber.isUnsubscribed()) {
          RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(view);
          subscriber.onNext(holder.getAdapterPosition());
          return true;
        }
        return false;
      }
    };

    final RecyclerView.OnChildAttachStateChangeListener onAttachListener = new RecyclerView.OnChildAttachStateChangeListener() {
      @Override public void onChildViewAttachedToWindow(View view) {
        view.setOnLongClickListener(onLongClickListener);
      }

      @Override public void onChildViewDetachedFromWindow(View view) {
        view.setOnLongClickListener(null);
      }
    };

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        recyclerView.removeOnChildAttachStateChangeListener(onAttachListener);
      }
    });

    recyclerView.addOnChildAttachStateChangeListener(onAttachListener);
  }
}
