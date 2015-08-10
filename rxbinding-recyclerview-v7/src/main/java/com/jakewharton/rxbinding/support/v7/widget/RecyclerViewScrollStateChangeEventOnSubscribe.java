package com.jakewharton.rxbinding.support.v7.widget;

import android.support.v7.widget.RecyclerView;

import com.jakewharton.rxbinding.internal.MainThreadSubscription;

import rx.Observable;
import rx.Subscriber;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class RecyclerViewScrollStateChangeEventOnSubscribe
    implements Observable.OnSubscribe<RecyclerViewScrollStateChangeEvent> {

  private final RecyclerView recyclerView;

  public RecyclerViewScrollStateChangeEventOnSubscribe(RecyclerView recyclerView) {
    this.recyclerView = recyclerView;
  }

  @Override public void call(final Subscriber<? super RecyclerViewScrollStateChangeEvent> subscriber) {
    checkUiThread();

    final RecyclerView.OnScrollListener listener = new RecyclerView.OnScrollListener() {
      @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(RecyclerViewScrollStateChangeEvent.create(recyclerView, newState));
        }
      }
    };
    recyclerView.addOnScrollListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        recyclerView.removeOnScrollListener(listener);
      }
    });
  }
}
