package com.jakewharton.rxbinding2.support.v7.widget;

import android.support.v7.widget.SearchView;
import com.jakewharton.rxbinding2.InitialValueObservable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

final class SearchViewQueryTextChangesObservable extends InitialValueObservable<CharSequence> {
  private final SearchView view;

  SearchViewQueryTextChangesObservable(SearchView view) {
    this.view = view;
  }

  @Override protected void subscribeListener(Observer<? super CharSequence> observer) {
    if (!checkMainThread(observer)) {
      return;
    }
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.setOnQueryTextListener(listener);
  }

  @Override protected CharSequence getInitialValue() {
    return view.getQuery();
  }

  final class Listener extends MainThreadDisposable implements SearchView.OnQueryTextListener {
    private final SearchView searchView;
    private final Observer<? super CharSequence> observer;

    Listener(SearchView searchView, Observer<? super CharSequence> observer) {
      this.searchView = searchView;
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
      searchView.setOnQueryTextListener(null);
    }
  }
}
