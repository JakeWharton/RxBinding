package rx.android.widget;

import android.view.View;
import android.widget.AdapterView;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.internal.AndroidSubscriptions;
import rx.functions.Action0;

import static rx.android.internal.Preconditions.checkUiThread;

final class AdapterViewItemSelectionOnSubscribe implements Observable.OnSubscribe<Integer> {
  private final AdapterView<?> view;

  public AdapterViewItemSelectionOnSubscribe(AdapterView<?> view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Integer> subscriber) {
    checkUiThread();

    AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        subscriber.onNext(position);
      }

      @Override public void onNothingSelected(AdapterView<?> parent) {
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
