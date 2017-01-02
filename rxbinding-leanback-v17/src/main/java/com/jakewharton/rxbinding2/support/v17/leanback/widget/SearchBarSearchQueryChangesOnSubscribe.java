package com.jakewharton.rxbinding2.support.v17.leanback.widget;

import android.support.v17.leanback.widget.SearchBar;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class SearchBarSearchQueryChangesOnSubscribe extends Observable<String> {
  final SearchBar view;

  SearchBarSearchQueryChangesOnSubscribe(SearchBar view) {
    this.view = view;
  }

  @Override protected void subscribeActual(final Observer<? super String> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.setSearchBarListener(listener);
  }

  static class Listener extends MainThreadDisposable implements SearchBar.SearchBarListener {
    final SearchBar view;
    final Observer<? super String> observer;

    Listener(SearchBar view, Observer<? super String> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override public void onSearchQueryChange(String query) {
      if (!isDisposed()) {
        observer.onNext(query);
      }
    }

    @Override public void onSearchQuerySubmit(String query) {
    }

    @Override public void onKeyboardDismiss(String query) {
    }

    @Override protected void onDispose() {
      view.setSearchBarListener(null);
    }
  }
}
