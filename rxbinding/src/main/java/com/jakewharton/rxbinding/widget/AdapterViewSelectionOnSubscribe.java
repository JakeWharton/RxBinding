package com.jakewharton.rxbinding.widget;

import android.view.View;
import android.widget.AdapterView;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import com.jakewharton.rxbinding.internal.AndroidSubscriptions;
import rx.functions.Action0;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class AdapterViewSelectionOnSubscribe
    implements Observable.OnSubscribe<AdapterViewSelectionEvent> {
  private final AdapterView<?> view;

  public AdapterViewSelectionOnSubscribe(AdapterView<?> view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super AdapterViewSelectionEvent> subscriber) {
    checkUiThread();

    AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        subscriber.onNext(AdapterViewItemSelectionEvent.create(parent, view, position, id));
      }

      @Override public void onNothingSelected(AdapterView<?> parent) {
        subscriber.onNext(AdapterViewNothingSelectionEvent.create(parent));
      }
    };

    Subscription subscription = AndroidSubscriptions.unsubscribeOnMainThread(new Action0() {
      @Override public void call() {
        view.setOnItemSelectedListener(null);
      }
    });
    subscriber.add(subscription);

    view.setOnItemSelectedListener(listener);
  }
}
