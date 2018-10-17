package com.jakewharton.rxbinding3.leanback;

import android.content.Context;
import android.view.KeyEvent;
import androidx.leanback.widget.SearchEditText;
import androidx.test.InstrumentationRegistry;
import androidx.test.annotation.UiThreadTest;
import com.jakewharton.rxbinding3.RecordingObserver;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public final class RxSearchEditTextTest {
  private final Context context = InstrumentationRegistry.getContext();
  private SearchEditText view;

  @Before public void setUp() {
    view = new SearchEditText(context);
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
