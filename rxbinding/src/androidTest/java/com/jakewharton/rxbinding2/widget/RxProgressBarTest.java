package com.jakewharton.rxbinding2.widget;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ProgressBar;
import io.reactivex.functions.Consumer;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public final class RxProgressBarTest {
  @Rule public final UiThreadTestRule uiThread = new UiThreadTestRule();

  private final Context context = InstrumentationRegistry.getContext();
  private final ProgressBar view = new ProgressBar(context, null, 0); // Explicit no defStyleAttr.

  @Test @UiThreadTest public void incrementProgressBy() throws Exception {
    Consumer<? super Integer> action = RxProgressBar.incrementProgressBy(view);
    assertEquals(0, view.getProgress());
    action.accept(10);
    assertEquals(10, view.getProgress());
    action.accept(20);
    assertEquals(30, view.getProgress());
    action.accept(30);
    assertEquals(60, view.getProgress());
    action.accept(40);
    assertEquals(100, view.getProgress());
  }

  @Test @UiThreadTest public void incrementSecondaryProgressBy() throws Exception {
    Consumer<? super Integer> action = RxProgressBar.incrementSecondaryProgressBy(view);
    assertEquals(0, view.getSecondaryProgress());
    action.accept(10);
    assertEquals(10, view.getSecondaryProgress());
    action.accept(20);
    assertEquals(30, view.getSecondaryProgress());
    action.accept(30);
    assertEquals(60, view.getSecondaryProgress());
    action.accept(40);
    assertEquals(100, view.getSecondaryProgress());
  }

  @Test @UiThreadTest public void indeterminate() throws Exception {
    Consumer<? super Boolean> action = RxProgressBar.indeterminate(view);
    action.accept(true);
    assertTrue(view.isIndeterminate());
    action.accept(false);
    assertFalse(view.isIndeterminate());
  }

  @Test @UiThreadTest public void max() throws Exception {
    Consumer<? super Integer> action = RxProgressBar.max(view);
    action.accept(100);
    assertEquals(100, view.getMax());
    action.accept(1000);
    assertEquals(1000, view.getMax());
  }

  @Test @UiThreadTest public void progress() throws Exception {
    Consumer<? super Integer> action = RxProgressBar.progress(view);
    assertEquals(0, view.getProgress());
    action.accept(50);
    assertEquals(50, view.getProgress());
    action.accept(100);
    assertEquals(100, view.getProgress());
  }

  @Test @UiThreadTest public void secondaryProgress() throws Exception {
    Consumer<? super Integer> action = RxProgressBar.secondaryProgress(view);
    assertEquals(0, view.getSecondaryProgress());
    action.accept(50);
    assertEquals(50, view.getSecondaryProgress());
    action.accept(100);
    assertEquals(100, view.getSecondaryProgress());
  }
}
