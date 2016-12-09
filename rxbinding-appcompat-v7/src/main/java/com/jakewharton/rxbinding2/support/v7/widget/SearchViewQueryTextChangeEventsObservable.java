package com.jakewharton.rxbinding2.support.v7.widget;

import android.support.v7.widget.SearchView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class SearchViewQueryTextChangeEventsObservable extends Observable<SearchViewQueryTextEvent> {
  private final SearchView view;

  SearchViewQueryTextChangeEventsObservable(SearchView view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super SearchViewQueryTextEvent> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.setOnQueryTextListener(listener);

    // Emit initial value.
    observer.onNext(SearchViewQueryTextEvent.create(view, view.getQuery(), false));
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
