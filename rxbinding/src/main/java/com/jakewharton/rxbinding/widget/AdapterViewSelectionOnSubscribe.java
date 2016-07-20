package com.jakewharton.rxbinding.widget;

import android.view.View;
import android.widget.AdapterView;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static android.widget.AdapterView.INVALID_POSITION;
import static rx.android.MainThreadSubscription.verifyMainThread;

final class AdapterViewSelectionOnSubscribe
    implements Observable.OnSubscribe<AdapterViewSelectionEvent> {
  final AdapterView<?> view;

  public AdapterViewSelectionOnSubscribe(AdapterView<?> view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super AdapterViewSelectionEvent> subscriber) {
    verifyMainThread();

    AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(AdapterViewItemSelectionEvent.create(parent, view, position, id));
        }
      }

      @Override public void onNothingSelected(AdapterView<?> parent) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(AdapterViewNothingSelectionEvent.create(parent));
        }
      }
    };
    view.setOnItemSelectedListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnItemSelectedListener(null);
      }
    });

    // Emit initial value.
    int selectedPosition = view.getSelectedItemPosition();
    if (selectedPosition == INVALID_POSITION) {
      subscriber.onNext(AdapterViewNothingSelectionEvent.create(view));
    } else {
      View selectedView = view.getSelectedView();
      long selectedId = view.getSelectedItemId();
      subscriber.onNext(
          AdapterViewItemSelectionEvent.create(view, selectedView, selectedPosition, selectedId));
    }
  }
}
