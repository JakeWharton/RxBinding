package rx.android.widget;

import android.database.DataSetObserver;
import android.widget.Adapter;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.internal.AndroidSubscriptions;
import rx.functions.Action0;

import static rx.android.internal.Preconditions.checkUiThread;

final class AdapterDataChangeOnSubscribe<T extends Adapter>
    implements Observable.OnSubscribe<T> {
  private final T adapter;

  public AdapterDataChangeOnSubscribe(T adapter) {
    this.adapter = adapter;
  }

  @Override public void call(final Subscriber<? super T> subscriber) {
    checkUiThread();

    final DataSetObserver observer = new DataSetObserver() {
      @Override public void onChanged() {
        subscriber.onNext(adapter);
      }
    };

    Subscription subscription = AndroidSubscriptions.unsubscribeOnMainThread(new Action0() {
      @Override public void call() {
        adapter.unregisterDataSetObserver(observer);
      }
    });
    subscriber.add(subscription);

    adapter.registerDataSetObserver(observer);
  }
}
