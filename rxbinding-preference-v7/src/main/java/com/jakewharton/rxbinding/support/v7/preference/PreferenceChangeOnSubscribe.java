package com.jakewharton.rxbinding.support.v7.preference;

import android.support.v7.preference.Preference;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import rx.Observable;
import rx.Subscriber;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class PreferenceChangeOnSubscribe implements Observable.OnSubscribe<Object> {
  private final Preference preference;

  public PreferenceChangeOnSubscribe(Preference preference) {
    this.preference = preference;
  }

  @Override public void call(final Subscriber<? super Object> subscriber) {
    checkUiThread();

    Preference.OnPreferenceChangeListener listener = new Preference.OnPreferenceChangeListener() {
      @Override public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(newValue);
        }
        return true;
      }
    };
    preference.setOnPreferenceChangeListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        preference.setOnPreferenceClickListener(null);
      }
    });
  }
}
