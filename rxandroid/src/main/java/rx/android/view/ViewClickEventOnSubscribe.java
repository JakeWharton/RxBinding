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

final class ViewClickEventOnSubscribe implements Observable.OnSubscribe<ViewClickEvent> {
  private final View view;

  ViewClickEventOnSubscribe(View view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super ViewClickEvent> subscriber) {
    checkUiThread();

    final RxAndroidClockHook clockHook = RxAndroidPlugins.getInstance().getClockHook();
    View.OnClickListener listener = new View.OnClickListener() {
      @Override public void onClick(View v) {
        subscriber.onNext(ViewClickEvent.create(view, clockHook.uptimeMillis()));
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
