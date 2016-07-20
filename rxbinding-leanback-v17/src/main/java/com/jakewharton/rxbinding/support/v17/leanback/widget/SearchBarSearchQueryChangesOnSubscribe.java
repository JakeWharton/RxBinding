package com.jakewharton.rxbinding.support.v17.leanback.widget;

import android.support.v17.leanback.widget.SearchBar;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class SearchBarSearchQueryChangesOnSubscribe implements Observable.OnSubscribe<String> {
  final SearchBar view;

  SearchBarSearchQueryChangesOnSubscribe(SearchBar view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super String> subscriber) {
    verifyMainThread();

    SearchBar.SearchBarListener listener = new SearchBar.SearchBarListener() {

      @Override public void onSearchQueryChange(String query) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(query);
        }
      }

      @Override public void onSearchQuerySubmit(String query) {
      }

      @Override public void onKeyboardDismiss(String query) {
      }
    };

    view.setSearchBarListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setSearchBarListener(null);
      }
    });
  }
}
