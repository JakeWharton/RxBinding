package com.jakewharton.rxbinding2.widget;

import android.widget.SearchView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class SearchViewQueryTextChangesObservable extends Observable<CharSequence> {
  private final SearchView view;

  SearchViewQueryTextChangesObservable(SearchView view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super CharSequence> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    view.setOnQueryTextListener(listener);
    observer.onSubscribe(listener);
    observer.onNext(view.getQuery());
  }

  static final class Listener extends MainThreadDisposable
          implements SearchView.OnQueryTextListener {

    private final SearchView view;
    private final Observer<? super CharSequence> observer;

    public Listener(SearchView view, Observer<? super CharSequence> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override public boolean onQueryTextChange(String s) {
      if (!isDisposed()) {
        observer.onNext(s);
        return true;
      }
      return false;
    }

    @Override public boolean onQueryTextSubmit(String query) {
      return false;
    }

    @Override protected void onDispose() {
      view.setOnQueryTextListener(null);
    }
  }
}
