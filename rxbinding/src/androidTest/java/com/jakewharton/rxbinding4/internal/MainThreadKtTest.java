package com.jakewharton.rxbinding4.internal;

import androidx.test.annotation.UiThreadTest;
import com.jakewharton.rxbinding4.RecordingObserver;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class MainThreadKtTest {
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
