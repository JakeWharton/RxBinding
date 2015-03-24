package rx.android.view;

import android.view.View;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.internal.AndroidSubscriptions;
import rx.android.plugins.RxAndroidClockHook;
import rx.android.plugins.RxAndroidPlugins;
import rx.functions.Action0;

import static rx.android.internal.Preconditions.checkUiThread;

final class ViewFocusChangeEventOnSubscribe
    implements Observable.OnSubscribe<ViewFocusChangeEvent> {
  private final View view;

  public ViewFocusChangeEventOnSubscribe(View view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super ViewFocusChangeEvent> subscriber) {
    checkUiThread();

    final RxAndroidClockHook clockHook = RxAndroidPlugins.getInstance().getClockHook();
    View.OnFocusChangeListener listener = new View.OnFocusChangeListener() {
      @Override public void onFocusChange(View v, boolean hasFocus) {
        subscriber.onNext(ViewFocusChangeEvent.create(view, clockHook.uptimeMillis(), hasFocus));
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
    subscriber.onNext(ViewFocusChangeEvent.create(view, clockHook.uptimeMillis(), view.hasFocus()));
  }
}
