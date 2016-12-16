package com.jakewharton.rxbinding2.widget;

import android.widget.SearchView;
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
    view.setOnQueryTextListener(listener);
    observer.onSubscribe(listener);
    observer.onNext(SearchViewQueryTextEvent.create(view, view.getQuery(), false));
  }

  static final class Listener extends MainThreadDisposable
          implements SearchView.OnQueryTextListener {

    private final SearchView view;
    private final Observer<? super SearchViewQueryTextEvent> observer;

    Listener(SearchView view, Observer<? super SearchViewQueryTextEvent> observer) {
      this.view = view;
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
        observer.onNext(SearchViewQueryTextEvent.create(view, query, true));
        return true;
      }
      return false;
    }

    @Override protected void onDispose() {
      view.setOnQueryTextListener(null);
    }
  }
}
