package com.jakewharton.rxbinding.support.v17.leanback.widget;

import com.jakewharton.rxbinding.internal.MainThreadSubscription;

import android.support.v17.leanback.widget.SearchBar;

import rx.Observable;
import rx.Subscriber;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class SearchBarSearchQueryChangeEventsOnSubscribe
    implements Observable.OnSubscribe<SearchBarSearchQueryEvent> {
  private final SearchBar view;

  SearchBarSearchQueryChangeEventsOnSubscribe(SearchBar view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super SearchBarSearchQueryEvent> subscriber) {
    checkUiThread();

    SearchBar.SearchBarListener listener = new SearchBar.SearchBarListener() {

      @Override public void onSearchQueryChange(String query) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(SearchBarSearchQueryEvent.create(view, query, SearchBarSearchQueryEvent.Kind.CHANGED));
        }
      }

      @Override public void onSearchQuerySubmit(String query) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(SearchBarSearchQueryEvent.create(view, query, SearchBarSearchQueryEvent.Kind.SUBMITTED));
        }
      }

      @Override public void onKeyboardDismiss(String query) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(SearchBarSearchQueryEvent.create(view, query, SearchBarSearchQueryEvent.Kind.KEYBOARD_DISMISSED));
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
