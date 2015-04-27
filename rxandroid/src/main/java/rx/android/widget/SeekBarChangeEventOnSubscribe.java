package rx.android.widget;

import android.widget.SeekBar;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.internal.AndroidSubscriptions;
import rx.functions.Action0;

import static rx.android.internal.Preconditions.checkUiThread;

final class SeekBarChangeEventOnSubscribe
    implements Observable.OnSubscribe<SeekBarChangeEvent> {
  private final SeekBar view;

  public SeekBarChangeEventOnSubscribe(SeekBar view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super SeekBarChangeEvent> subscriber) {
    checkUiThread();

    SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
      @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        subscriber.onNext(SeekBarProgressChangeEvent.create(seekBar, progress, fromUser));
      }

      @Override public void onStartTrackingTouch(SeekBar seekBar) {
        subscriber.onNext(SeekBarStartChangeEvent.create(seekBar));
      }

      @Override public void onStopTrackingTouch(SeekBar seekBar) {
        subscriber.onNext(SeekBarStopChangeEvent.create(seekBar));
      }
    };

    Subscription subscription = AndroidSubscriptions.unsubscribeOnMainThread(new Action0() {
      @Override public void call() {
        view.setOnSeekBarChangeListener(null);
      }
    });
    subscriber.add(subscription);

    view.setOnSeekBarChangeListener(listener);
  }
}
