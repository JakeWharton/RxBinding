package com.jakewharton.rxbinding2.widget;

import android.widget.RatingBar;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class RatingBarRatingChangeObservable extends Observable<Float> {
  private final RatingBar view;

  RatingBarRatingChangeObservable(RatingBar view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super Float> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    view.setOnRatingBarChangeListener(listener);
    observer.onSubscribe(listener);
    observer.onNext(view.getRating());
  }

  static final class Listener extends MainThreadDisposable
          implements RatingBar.OnRatingBarChangeListener {

    private final RatingBar view;
    private final Observer<? super Float> observer;

    Listener(RatingBar view, Observer<? super Float> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
      if (!isDisposed()) {
        observer.onNext(rating);
      }
    }

    @Override protected void onDispose() {
      view.setOnRatingBarChangeListener(null);
    }
  }
}
