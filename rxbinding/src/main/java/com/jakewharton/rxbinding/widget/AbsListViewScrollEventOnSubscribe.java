package com.jakewharton.rxbinding.widget;

import android.widget.AbsListView;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class AbsListViewScrollEventOnSubscribe
    implements Observable.OnSubscribe<AbsListViewScrollEvent> {

  final AbsListView view;

  public AbsListViewScrollEventOnSubscribe(AbsListView view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super AbsListViewScrollEvent> subscriber) {
    checkUiThread();

    final AbsListView.OnScrollListener listener = new AbsListView.OnScrollListener() {
      int currentScrollState = SCROLL_STATE_IDLE;

      @Override public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.currentScrollState = scrollState;
        if (!subscriber.isUnsubscribed()) {
          AbsListViewScrollEvent event =
              AbsListViewScrollEvent.create(view, scrollState, view.getFirstVisiblePosition(),
                  view.getChildCount(), view.getCount());
          subscriber.onNext(event);
        }
      }

      @Override public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
          int totalItemCount) {
        if (!subscriber.isUnsubscribed()) {
          AbsListViewScrollEvent event =
              AbsListViewScrollEvent.create(view, this.currentScrollState, firstVisibleItem,
                  visibleItemCount, totalItemCount);
          subscriber.onNext(event);
        }
      }
    };

    view.setOnScrollListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnScrollListener(null);
      }
    });
  }
}
