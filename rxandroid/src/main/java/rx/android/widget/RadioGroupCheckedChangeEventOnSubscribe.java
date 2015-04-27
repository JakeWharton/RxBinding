package rx.android.widget;

import android.widget.RadioGroup;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

import static rx.android.internal.Preconditions.checkUiThread;

final class RadioGroupCheckedChangeEventOnSubscribe
    implements Observable.OnSubscribe<RadioGroupCheckedChangeEvent> {
  private final RadioGroup view;

  public RadioGroupCheckedChangeEventOnSubscribe(RadioGroup view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super RadioGroupCheckedChangeEvent> subscriber) {
    checkUiThread();

    RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
        subscriber.onNext(RadioGroupCheckedChangeEvent.create(group, checkedId));
      }
    };

    Subscription subscription = Subscriptions.create(new Action0() {
      @Override public void call() {
        view.setOnCheckedChangeListener(null);
      }
    });
    subscriber.add(subscription);

    view.setOnCheckedChangeListener(listener);

  }
}
