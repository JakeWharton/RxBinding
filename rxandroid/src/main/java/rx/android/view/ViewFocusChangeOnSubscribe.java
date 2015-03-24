package rx.android.view;

import android.view.View;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.internal.AndroidSubscriptions;
import rx.functions.Action0;

import static rx.android.internal.Preconditions.checkUiThread;

final class ViewFocusChangeOnSubscribe implements Observable.OnSubscribe<Boolean> {
  private final View view;

  public ViewFocusChangeOnSubscribe(View view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Boolean> subscriber) {
    checkUiThread();

    View.OnFocusChangeListener listener = new View.OnFocusChangeListener() {
      @Override public void onFocusChange(View v, boolean hasFocus) {
        subscriber.onNext(hasFocus);
      }
    };

    Subscription subscription = AndroidSubscriptions.unsubscribeOnMainThread(new Action0() {
      @Override public void call() {
        view.setOnFocusChangeListener(null);
      }
    });
    subscriber.add(subscription);

    view.setOnFocusChangeListener(listener);

    // Send out the initial value.
    subscriber.onNext(view.hasFocus());
  }
}
