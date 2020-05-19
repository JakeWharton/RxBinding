package com.jakewharton.rxbinding4.leanback;

import android.view.KeyEvent;
import androidx.leanback.widget.SearchEditText;
import androidx.test.annotation.UiThreadTest;
import androidx.test.rule.ActivityTestRule;
import com.jakewharton.rxbinding4.RecordingObserver;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public final class RxSearchEditTextTest {
  @Rule public final ActivityTestRule<RxSearchEditTextTestActivity> activityRule =
          new ActivityTestRule<>(RxSearchEditTextTestActivity.class);
  private SearchEditText view;

  @Before public void setUp() {
    view = activityRule.getActivity().searchEditText;
  }

  @Test @UiThreadTest public void keyboardDismisses() {
    RecordingObserver<Object> o = new RecordingObserver<>();
    RxSearchEditText.keyboardDismisses(view).subscribe(o);
    o.assertNoMoreEvents();

    KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);

    view.onKeyPreIme(KeyEvent.KEYCODE_BACK, event);
    assertNotNull(o.takeNext());

    o.dispose();

    view.onKeyPreIme(KeyEvent.KEYCODE_BACK, event);
    o.assertNoMoreEvents();
  }
}
