package com.jakewharton.rxbinding.support.v7.preference;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v7.preference.Preference;
import rx.Observable;
import rx.functions.Action1;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables} and {@linkplain Action1
 * actions} for {@link Preference}.
 */
public final class RxPreference {
  /**
   * Create an observable which emits on {@code Preference} click events. The emitted value is
   * unspecified and should only be used as notification.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code preference}. Unsubscribe
   * to free this reference.
   * <em>Warning:</em> The created observable uses {@link Preference#setOnPreferenceClickListener} to observe
   * clicks. Only one observable can be used for a preference at a time.
   */
  @CheckResult @NonNull
  public static Observable<Void> clicks(@NonNull Preference preference) {
    checkNotNull(preference, "preference == null");
    return Observable.create(new PreferenceClickOnSubscribe(preference));
  }

  /**
   * Create an observable which emits on {@code Preference} change events.
   * <p>
   * <em>Warning:</em> The created observable keeps a strong reference to {@code preference}. Unsubscribe
   * to free this reference.
   * <em>Warning:</em> The created observable uses {@link Preference#setOnPreferenceChangeListener} to observe
   * changes. Only one observable can be used for a preference at a time.
   */
  @CheckResult @NonNull
  public static Observable<Object> changes(@NonNull Preference preference) {
    checkNotNull(preference, "preference == null");
    return Observable.create(new PreferenceChangeOnSubscribe(preference));
  }

  private RxPreference() {
    throw new AssertionError("No instances.");
  }
}
