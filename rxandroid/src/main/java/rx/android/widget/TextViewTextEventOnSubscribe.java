package rx.android.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.internal.AndroidSubscriptions;
import rx.android.plugins.RxAndroidClockHook;
import rx.android.plugins.RxAndroidPlugins;
import rx.functions.Action0;

import static rx.android.internal.Preconditions.checkUiThread;

final class TextViewTextEventOnSubscribe implements Observable.OnSubscribe<TextViewTextChangeEvent> {
  private final TextView view;

  public TextViewTextEventOnSubscribe(TextView view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super TextViewTextChangeEvent> subscriber) {
    checkUiThread();

    final RxAndroidClockHook clockHook = RxAndroidPlugins.getInstance().getClockHook();
    final TextWatcher watcher = new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
        subscriber.onNext(
            TextViewTextChangeEvent.create(view, clockHook.uptimeMillis(), s, start, before,
                count));
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
    subscriber.onNext(
        TextViewTextChangeEvent.create(view, clockHook.uptimeMillis(), view.getText(), 0, 0, 0));
  }
}
