package rx.android.view;

import android.view.View;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.internal.AndroidSubscriptions;
import rx.android.plugins.RxAndroidClockHook;
import rx.android.plugins.RxAndroidPlugins;
import rx.functions.Action0;
import rx.functions.Func1;

import static rx.android.internal.Preconditions.checkUiThread;

final class ViewLongClickOnSubscribe implements Observable.OnSubscribe<Long> {
  private final View view;
  private final Func1<? super Long, Boolean> handled;

  ViewLongClickOnSubscribe(View view, Func1<? super Long, Boolean> handled) {
    this.view = view;
    this.handled = handled;
  }

  @Override public void call(final Subscriber<? super Long> subscriber) {
    checkUiThread();

    final RxAndroidClockHook clockHook = RxAndroidPlugins.getInstance().getClockHook();
    View.OnLongClickListener listener = new View.OnLongClickListener() {
      @Override public boolean onLongClick(View v) {
        long timestamp = clockHook.uptimeMillis();
        if (handled.call(timestamp)) {
          subscriber.onNext(timestamp);
          return true;
        }
        return false;
      }
    };

    Subscription subscription = AndroidSubscriptions.unsubscribeOnMainThread(new Action0() {
      @Override public void call() {
        view.setOnLongClickListener(null);
      }
    });
    subscriber.add(subscription);

    view.setOnLongClickListener(listener);
  }
}
