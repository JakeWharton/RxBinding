package com.jakewharton.rxbinding.support.v7.preference;

import android.support.v7.preference.Preference;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import rx.Observable;
import rx.Subscriber;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class PreferenceClickOnSubscribe implements Observable.OnSubscribe<Void> {
  private final Preference preference;

  public PreferenceClickOnSubscribe(Preference preference) {
    this.preference = preference;
  }

  @Override public void call(final Subscriber<? super Void> subscriber) {
    checkUiThread();

    Preference.OnPreferenceClickListener listener = new Preference.OnPreferenceClickListener() {
      @Override public boolean onPreferenceClick(Preference preference) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(null);
        }
        return true;
      }
    };
    preference.setOnPreferenceClickListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        preference.setOnPreferenceClickListener(null);
      }
    });
  }
}
