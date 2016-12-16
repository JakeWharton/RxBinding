package com.jakewharton.rxbinding2.widget;

import android.widget.RatingBar;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static io.reactivex.android.MainThreadDisposable.verifyMainThread;

final class RatingBarRatingChangeEventObservable extends Observable<RatingBarChangeEvent> {
  private final RatingBar view;

  RatingBarRatingChangeEventObservable(RatingBar view) {
    this.view = view;
  }

  @Override protected void subscribeActual(Observer<? super RatingBarChangeEvent> observer) {
    verifyMainThread();
    Listener listener = new Listener(view, observer);
    view.setOnRatingBarChangeListener(listener);
    observer.onSubscribe(listener);
    observer.onNext(RatingBarChangeEvent.create(view, view.getRating(), false));
  }

  static final class Listener extends MainThreadDisposable
          implements RatingBar.OnRatingBarChangeListener {

    private final RatingBar view;
    private final Observer<? super RatingBarChangeEvent> observer;

    Listener(RatingBar view, Observer<? super RatingBarChangeEvent> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
      if (!isDisposed()) {
        observer.onNext(RatingBarChangeEvent.create(ratingBar, rating, fromUser));
      }
    }

    @Override protected void onDispose() {
      view.setOnRatingBarChangeListener(null);
    }
  }
}
