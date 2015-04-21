package rx.android.view;

import android.view.DragEvent;
import android.view.View;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.internal.AndroidSubscriptions;
import rx.functions.Action0;
import rx.functions.Func1;

import static rx.android.internal.Preconditions.checkUiThread;

final class ViewDragEventOnSubscribe implements Observable.OnSubscribe<ViewDragEvent> {
  private final View view;
  private final Func1<? super ViewDragEvent, Boolean> handled;

  public ViewDragEventOnSubscribe(View view, Func1<? super ViewDragEvent, Boolean> handled) {
    this.view = view;
    this.handled = handled;
  }

  @Override public void call(final Subscriber<? super ViewDragEvent> subscriber) {
    checkUiThread();

    View.OnDragListener listener = new View.OnDragListener() {
      @Override public boolean onDrag(View v, DragEvent dragEvent) {
        ViewDragEvent event = ViewDragEvent.create(view, dragEvent);
        if (handled.call(event)) {
          subscriber.onNext(event);
          return true;
        }
        return false;
      }
    };

    Subscription subscription = AndroidSubscriptions.unsubscribeOnMainThread(new Action0() {
      @Override public void call() {
        view.setOnDragListener(null);
      }
    });
    subscriber.add(subscription);

    view.setOnDragListener(listener);
  }
}
