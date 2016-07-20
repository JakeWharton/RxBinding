package com.jakewharton.rxbinding.support.v17.leanback.widget;

import android.support.v17.leanback.widget.SearchBar;
import com.jakewharton.rxbinding.support.v17.leanback.widget.SearchBarSearchQueryEvent.Kind;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class SearchBarSearchQueryChangeEventsOnSubscribe
    implements Observable.OnSubscribe<SearchBarSearchQueryEvent> {
  final SearchBar view;

  SearchBarSearchQueryChangeEventsOnSubscribe(SearchBar view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super SearchBarSearchQueryEvent> subscriber) {
    verifyMainThread();

    SearchBar.SearchBarListener listener = new SearchBar.SearchBarListener() {

      @Override public void onSearchQueryChange(String query) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(SearchBarSearchQueryEvent.create(view, query, Kind.CHANGED));
        }
      }

      @Override public void onSearchQuerySubmit(String query) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(SearchBarSearchQueryEvent.create(view, query, Kind.SUBMITTED));
        }
      }

      @Override public void onKeyboardDismiss(String query) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(SearchBarSearchQueryEvent.create(view, query, Kind.KEYBOARD_DISMISSED));
        }
      }
    };

    view.setSearchBarListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override
      protected void onUnsubscribe() {
        view.setSearchBarListener(null);
      }
    });
  }
}
