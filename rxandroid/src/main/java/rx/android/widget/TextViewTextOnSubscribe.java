package rx.android.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.internal.AndroidSubscriptions;
import rx.functions.Action0;

import static rx.android.internal.Preconditions.checkUiThread;

final class TextViewTextOnSubscribe implements Observable.OnSubscribe<CharSequence> {
  private final TextView view;

  public TextViewTextOnSubscribe(TextView view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super CharSequence> subscriber) {
    checkUiThread();

    final TextWatcher watcher = new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
        subscriber.onNext(s);
      }

      @Override public void afterTextChanged(Editable s) {
      }
    };

    Subscription subscription = AndroidSubscriptions.unsubscribeOnMainThread(new Action0() {
      @Override public void call() {
        view.removeTextChangedListener(watcher);
      }
    });
    subscriber.add(subscription);

    view.addTextChangedListener(watcher);

    // Send out the initial value.
    subscriber.onNext(view.getText());
  }
}
