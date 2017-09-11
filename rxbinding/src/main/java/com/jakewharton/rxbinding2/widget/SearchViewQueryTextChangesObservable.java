package com.jakewharton.rxbinding2.widget;

import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
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
    view.setOnQueryTextListener(listener);
    observer.onSubscribe(listener);
  }

  @Override protected CharSequence getInitialValue() {
    return view.getQuery();
  }

  static final class Listener extends MainThreadDisposable implements OnQueryTextListener {
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
