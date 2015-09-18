package com.jakewharton.rxbinding.support.design.widget;

import android.support.design.widget.AppBarLayout;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import rx.Observable;
import rx.Subscriber;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class AppBarLayoutOffsetChangeEventOnSubscribe
    implements Observable.OnSubscribe<AppBarLayoutOffsetChangeEvent> {
  private final AppBarLayout view;

  public AppBarLayoutOffsetChangeEventOnSubscribe(AppBarLayout view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super AppBarLayoutOffsetChangeEvent> subscriber) {
    checkUiThread();

    final AppBarLayout.OnOffsetChangedListener listener = new AppBarLayout.OnOffsetChangedListener() {
      @Override public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(AppBarLayoutOffsetChangeEvent.create(appBarLayout, verticalOffset));
        }
      }
    };
    view.addOnOffsetChangedListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.removeOnOffsetChangedListener(listener);
      }
    });
  }
}
