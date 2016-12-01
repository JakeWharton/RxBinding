package com.jakewharton.rxbinding.support.v7.widget;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnChildAttachStateChangeListener;
import android.view.View;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class RecyclerViewItemClickOnSubscribe implements Observable.OnSubscribe<Integer> {
  final RecyclerView recyclerView;

  public RecyclerViewItemClickOnSubscribe(RecyclerView recyclerView) {
    this.recyclerView = recyclerView;
  }

  @Override public void call(final Subscriber<? super Integer> subscriber) {
    verifyMainThread();

    final View.OnClickListener onClickListener = new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (!subscriber.isUnsubscribed()) {
          RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(view);
          subscriber.onNext(holder.getAdapterPosition());
        }
      }
    };

    final OnChildAttachStateChangeListener onAttachListener = new OnChildAttachStateChangeListener() {
      @Override public void onChildViewAttachedToWindow(View view) {
        view.setOnClickListener(onClickListener);
      }

      @Override public void onChildViewDetachedFromWindow(View view) {
        view.setOnClickListener(null);
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
