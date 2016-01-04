package com.jakewharton.rxbinding.support.v7.widget;

import android.support.v7.widget.SearchView;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;
import rx.functions.Func1;

final class SearchViewSuggestionClickOnSubscribe
        implements Observable.OnSubscribe<Integer> {
  final SearchView view;
  final Func1<? super Integer, Boolean> handled;

  SearchViewSuggestionClickOnSubscribe(SearchView view,
                                       Func1<? super Integer, Boolean> handled) {
    this.view = view;
    this.handled = handled;
  }

  @Override public void call(final Subscriber<? super Integer> subscriber) {
    SearchView.OnSuggestionListener listener = new SearchView.OnSuggestionListener() {
      @Override public boolean onSuggestionSelect(int position) {
        return false;
      }

      @Override public boolean onSuggestionClick(int position) {
        if (handled.call(position)) {
          if (!subscriber.isUnsubscribed()) {
            subscriber.onNext(position);
          }
          return true;
        }
        return false;
      }
    };

    view.setOnSuggestionListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnSuggestionListener(null);
      }
    });
  }
}
