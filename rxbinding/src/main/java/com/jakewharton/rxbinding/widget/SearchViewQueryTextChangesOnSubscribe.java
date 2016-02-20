package com.jakewharton.rxbinding.widget;

import android.widget.SearchView;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class SearchViewQueryTextChangesOnSubscribe implements Observable.OnSubscribe<CharSequence> {
  final SearchView view;

  SearchViewQueryTextChangesOnSubscribe(SearchView view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super CharSequence> subscriber) {
    verifyMainThread();

    SearchView.OnQueryTextListener watcher = new SearchView.OnQueryTextListener() {
      @Override public boolean onQueryTextChange(String s) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(s);
          return true;
        }
        return false;
      }

      @Override public boolean onQueryTextSubmit(String query) {
        return false;
      }
    };

    view.setOnQueryTextListener(watcher);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnQueryTextListener(null);
      }
    });

    // Emit initial value.
    subscriber.onNext(view.getQuery());
  }
}
