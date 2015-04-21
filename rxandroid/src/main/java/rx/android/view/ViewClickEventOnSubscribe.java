package rx.android.view;

import android.view.View;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.internal.AndroidSubscriptions;
import rx.functions.Action0;

import static rx.android.internal.Preconditions.checkUiThread;

final class ViewClickEventOnSubscribe implements Observable.OnSubscribe<ViewClickEvent> {
  private final View view;

  ViewClickEventOnSubscribe(View view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super ViewClickEvent> subscriber) {
    checkUiThread();

    View.OnClickListener listener = new View.OnClickListener() {
      @Override public void onClick(View v) {
        subscriber.onNext(ViewClickEvent.create(view));
      }
    };

    Subscription subscription = AndroidSubscriptions.unsubscribeOnMainThread(new Action0() {
      @Override public void call() {
        view.setOnClickListener(null);
      }
    });
    subscriber.add(subscription);

    view.setOnClickListener(listener);
  }
}
