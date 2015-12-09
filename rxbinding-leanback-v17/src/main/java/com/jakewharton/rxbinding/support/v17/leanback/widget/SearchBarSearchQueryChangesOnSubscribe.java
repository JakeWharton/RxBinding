package com.jakewharton.rxbinding.support.v17.leanback.widget;

import android.support.v17.leanback.widget.SearchBar;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class SearchBarSearchQueryChangesOnSubscribe implements Observable.OnSubscribe<String> {
  private final SearchBar view;

  SearchBarSearchQueryChangesOnSubscribe(SearchBar view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super String> subscriber) {
    checkUiThread();

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
