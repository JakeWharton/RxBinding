package com.jakewharton.rxbinding2.support.v17.leanback.widget;

import android.support.v17.leanback.widget.SearchBar;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

final class SearchBarSearchQueryChangeEventsOnSubscribe
    extends Observable<SearchBarSearchQueryEvent> {
  final SearchBar view;

  SearchBarSearchQueryChangeEventsOnSubscribe(SearchBar view) {
    this.view = view;
  }

  @Override
  protected void subscribeActual(final Observer<? super SearchBarSearchQueryEvent> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.setSearchBarListener(listener);
  }

  static final class Listener extends MainThreadDisposable implements SearchBar.SearchBarListener {
    final SearchBar view;
    final Observer<? super SearchBarSearchQueryEvent> observer;

    Listener(SearchBar view, Observer<? super SearchBarSearchQueryEvent> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override public void onSearchQueryChange(String query) {
      if (!isDisposed()) {
        observer.onNext(SearchBarSearchQueryChangedEvent.create(view, query));
      }
    }

    @Override public void onSearchQuerySubmit(String query) {
      if (!isDisposed()) {
        observer.onNext(SearchBarSearchQuerySubmittedEvent.create(view, query));
      }
    }

    @Override public void onKeyboardDismiss(String query) {
      if (!isDisposed()) {
        observer.onNext(SearchBarSearchQueryKeyboardDismissedEvent.create(view, query));
      }
    }

    @Override protected void onDispose() {
      view.setSearchBarListener(null);
    }
  }
}
