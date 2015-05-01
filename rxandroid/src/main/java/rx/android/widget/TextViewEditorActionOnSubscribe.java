package rx.android.widget;

import android.view.KeyEvent;
import android.widget.TextView;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

import static rx.android.internal.Preconditions.checkUiThread;

final class TextViewEditorActionOnSubscribe implements Observable.OnSubscribe<Integer> {
  private final TextView view;
  private final Func1<? super Integer, Boolean> handled;

  public TextViewEditorActionOnSubscribe(TextView view, Func1<? super Integer, Boolean> handled) {
    this.view = view;
    this.handled = handled;
  }

  @Override public void call(final Subscriber<? super Integer> subscriber) {
    checkUiThread();

    TextView.OnEditorActionListener listener = new TextView.OnEditorActionListener() {
      @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (handled.call(actionId)) {
          subscriber.onNext(actionId);
          return true;
        }
        return false;
      }
    };

    Subscription subscription = Subscriptions.create(new Action0() {
      @Override public void call() {
        view.setOnEditorActionListener(null);
      }
    });
    subscriber.add(subscription);

    view.setOnEditorActionListener(listener);
  }
}
