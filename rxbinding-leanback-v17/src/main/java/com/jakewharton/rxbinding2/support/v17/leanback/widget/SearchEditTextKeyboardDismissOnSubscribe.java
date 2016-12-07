package com.jakewharton.rxbinding2.support.v17.leanback.widget;

import android.support.v17.leanback.widget.SearchEditText;
import android.support.v17.leanback.widget.SearchEditText.OnKeyboardDismissListener;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class SearchEditTextKeyboardDismissOnSubscribe extends Observable<Void> {
  final SearchEditText view;

  SearchEditTextKeyboardDismissOnSubscribe(SearchEditText view) {
    this.view = view;
  }

  @Override protected void subscribeActual(final Observer<? super Void> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    observer.onSubscribe(listener);
    view.setOnKeyboardDismissListener(listener);
  }

  static class Listener extends MainThreadDisposable implements OnKeyboardDismissListener {
    final SearchEditText view;
    final Observer<? super Void> observer;

    Listener(final SearchEditText view, final Observer<? super Void> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override public void onKeyboardDismiss() {
      if (!isDisposed()) {
        observer.onNext(null); // TODO: can't be null need some generic object
      }
    }

    @Override protected void onDispose() {
      // TODO: set to null once http://b.android.com/187101 is released.
      view.setOnKeyboardDismissListener(new SearchEditText.OnKeyboardDismissListener() {
        @Override
        public void onKeyboardDismiss() {
        }
      });
    }
  }
}
