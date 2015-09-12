package com.jakewharton.rxbinding.support.v17.leanback.widget;

import com.jakewharton.rxbinding.RecordingObserver;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v17.leanback.widget.SearchEditText;
import android.view.KeyEvent;

import rx.Subscription;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public final class RxSearchEditTextTest {
  @Rule public final UiThreadTestRule uiThread = new UiThreadTestRule();

  private final Context context = InstrumentationRegistry.getContext();
  private final SearchEditText view = new SearchEditText(context);

  @Test @UiThreadTest public void keyboardDismisses() {
    RecordingObserver<Void> o = new RecordingObserver<>();
    Subscription subscription = RxSearchEditText.keyboardDismisses(view).subscribe(o);
    o.assertNoMoreEvents();

    KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);

    view.onKeyPreIme(KeyEvent.KEYCODE_BACK, event);
    assertThat(o.takeNext()).isNull();

    subscription.unsubscribe();

    view.onKeyPreIme(KeyEvent.KEYCODE_BACK, event);
    o.assertNoMoreEvents();
  }
}
