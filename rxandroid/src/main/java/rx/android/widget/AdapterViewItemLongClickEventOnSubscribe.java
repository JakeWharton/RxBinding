package rx.android.widget;

import android.view.View;
import android.widget.AdapterView;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.internal.AndroidSubscriptions;
import rx.functions.Action0;
import rx.functions.Func1;

import static rx.android.internal.Preconditions.checkUiThread;

final class AdapterViewItemLongClickEventOnSubscribe
    implements Observable.OnSubscribe<AdapterViewItemLongClickEvent> {
  private final AdapterView<?> view;
  private final Func1<? super AdapterViewItemLongClickEvent, Boolean> handled;

  public AdapterViewItemLongClickEventOnSubscribe(AdapterView<?> view,
      Func1<? super AdapterViewItemLongClickEvent, Boolean> handled) {
    this.view = view;
    this.handled = handled;
  }

  @Override public void call(final Subscriber<? super AdapterViewItemLongClickEvent> subscriber) {
    checkUiThread();

    AdapterView.OnItemLongClickListener listener = new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        AdapterViewItemLongClickEvent event =
            AdapterViewItemLongClickEvent.create(parent, view, position, id);
        if (handled.call(event)) {
          subscriber.onNext(event);
          return true;
        }
        return false;
      }
    };

    Subscription subscription = AndroidSubscriptions.unsubscribeOnMainThread(new Action0() {
      @Override public void call() {
        view.setOnItemLongClickListener(null);
      }
    });
    subscriber.add(subscription);

    view.setOnItemLongClickListener(listener);
  }
}
