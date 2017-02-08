package com.jakewharton.rxbinding2.support.v7.widget;

import android.support.v7.widget.SearchView;
import com.jakewharton.rxbinding2.InitialValueObservable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

final class SearchViewQueryTextChangeEventsObservable
    extends InitialValueObservable<SearchViewQueryTextEvent> {
  private final SearchView view;

  SearchViewQueryTextChangeEventsObservable(SearchView view) {
    this.view = view;
  }

  @Override protected void subscribeListener(Observer<? super SearchViewQueryTextEvent> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.setOnQueryTextListener(listener);
  }

  @Override protected SearchViewQueryTextEvent getInitialValue() {
    return SearchViewQueryTextEvent.create(view, view.getQuery(), false);
  }

  final class Listener extends MainThreadDisposable implements SearchView.OnQueryTextListener {
    private final SearchView searchView;
    private final Observer<? super SearchViewQueryTextEvent> observer;

    Listener(SearchView searchView, Observer<? super SearchViewQueryTextEvent> observer) {
      this.searchView = searchView;
      this.observer = observer;
    }

    @Override public boolean onQueryTextChange(String s) {
      if (!isDisposed()) {
        observer.onNext(SearchViewQueryTextEvent.create(view, s, false));
        return true;
      }
      return false;
    }

    @Override public boolean onQueryTextSubmit(String query) {
      if (!isDisposed()) {
        observer.onNext(SearchViewQueryTextEvent.create(view, view.getQuery(), true));
        return true;
      }
      return false;
    }

    @Override protected void onDispose() {
      searchView.setOnQueryTextListener(null);
    }
  }
}
