package com.jakewharton.rxbinding.support.v7.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import rx.Observable;
import rx.Subscriber;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class RecyclerViewItemClickOnSubscribe implements Observable.OnSubscribe<Integer> {

  private final RecyclerView recyclerView;

  RecyclerViewItemClickOnSubscribe(RecyclerView recyclerView) {
    this.recyclerView = recyclerView;
  }

  @Override public void call(final Subscriber<? super Integer> subscriber) {
    checkUiThread();

    final RecyclerView.OnChildAttachStateChangeListener childAttachStateChangeListener =
        new RecyclerView.OnChildAttachStateChangeListener() {
          @Override public void onChildViewAttachedToWindow(View view) {
            recyclerView.getChildViewHolder(view).itemView.setOnClickListener(
                new View.OnClickListener() {
                  @Override public void onClick(View v) {
                    if (!subscriber.isUnsubscribed()) {
                      subscriber.onNext(recyclerView.getChildAdapterPosition(v));
                    }
                  }
                });
          }

          @Override public void onChildViewDetachedFromWindow(View view) {
          }
        };

    recyclerView.addOnChildAttachStateChangeListener(childAttachStateChangeListener);
    recyclerView.swapAdapter(recyclerView.getAdapter(), true);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        recyclerView.removeOnChildAttachStateChangeListener(childAttachStateChangeListener);
      }
    });
  }
}
