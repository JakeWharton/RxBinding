package com.jakewharton.rxbinding2.internal;

import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import com.jakewharton.rxbinding2.RecordingObserver;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class PreconditionsTest {
  @Rule public final TestRule uiThread = new UiThreadTestRule();

  @UiThreadTest
  @Test public void checkMainOnMainDoesNotNotify() {
    RecordingObserver<Object> o = new RecordingObserver<>();
    assertTrue(Preconditions.checkMainThread(o));
    o.assertNoMoreEvents();
  }

  @Test public void checkMainOnNotMainNotifies() {
    RecordingObserver<Object> o = new RecordingObserver<>();
    assertFalse(Preconditions.checkMainThread(o));
    Throwable e = o.takeError();
    assertTrue(e instanceof IllegalStateException);
    assertTrue(e.getMessage().startsWith("Expected to be called on the main thread but was "));
  }
}
