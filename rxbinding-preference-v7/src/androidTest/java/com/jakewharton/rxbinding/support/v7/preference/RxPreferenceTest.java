package com.jakewharton.rxbinding.support.v7.preference;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.preference.Preference;
import com.jakewharton.rxbinding.RecordingObserver;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import rx.Subscription;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public final class RxPreferenceTest {
  @Rule public final UiThreadTestRule uiThread = new UiThreadTestRule();

  private final Context context = InstrumentationRegistry.getContext();
  private final Preference preference = new Preference(context);

  @Test @UiThreadTest public void clicks() {
    RecordingObserver<Void> o = new RecordingObserver<>();
    Subscription subscription = RxPreference.clicks(preference).subscribe(o);
    o.assertNoMoreEvents(); // No initial value.

    preference.performClick();
    assertThat(o.takeNext()).isNull();

    preference.performClick();
    assertThat(o.takeNext()).isNull();

    subscription.unsubscribe();

    preference.performClick();
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void changes() {
    RecordingObserver<Object> o = new RecordingObserver<>();
    Subscription subscription = RxPreference.changes(preference).subscribe(o);
    o.assertNoMoreEvents();

    Object newValue = new Object();
    preference.callChangeListener(newValue);
    assertThat(o.takeNext()).isSameAs(newValue);

    subscription.unsubscribe();
    preference.callChangeListener(newValue);
    o.assertNoMoreEvents();
  }
}
