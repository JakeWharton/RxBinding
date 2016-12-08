package com.jakewharton.rxbinding2.widget;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ProgressBar;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import io.reactivex.functions.Consumer;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public final class RxProgressBarTest {
  @Rule public final UiThreadTestRule uiThread = new UiThreadTestRule();

  private final Context context = InstrumentationRegistry.getContext();
  private final ProgressBar view = new ProgressBar(context, null, 0); // Explicit no defStyleAttr.

  @Test @UiThreadTest public void incrementProgressBy() throws Exception {
    Consumer<? super Integer> action = RxProgressBar.incrementProgressBy(view);
    assertThat(view.getProgress()).isEqualTo(0);
    action.accept(10);
    assertThat(view.getProgress()).isEqualTo(10);
    action.accept(20);
    assertThat(view.getProgress()).isEqualTo(30);
    action.accept(30);
    assertThat(view.getProgress()).isEqualTo(60);
    action.accept(40);
    assertThat(view.getProgress()).isEqualTo(100);
  }

  @Test @UiThreadTest public void incrementSecondaryProgressBy() throws Exception {
    Consumer<? super Integer> action = RxProgressBar.incrementSecondaryProgressBy(view);
    assertThat(view.getSecondaryProgress()).isEqualTo(0);
    action.accept(10);
    assertThat(view.getSecondaryProgress()).isEqualTo(10);
    action.accept(20);
    assertThat(view.getSecondaryProgress()).isEqualTo(30);
    action.accept(30);
    assertThat(view.getSecondaryProgress()).isEqualTo(60);
    action.accept(40);
    assertThat(view.getSecondaryProgress()).isEqualTo(100);
  }

  @Test @UiThreadTest public void indeterminate() throws Exception {
    Consumer<? super Boolean> action = RxProgressBar.indeterminate(view);
    action.accept(true);
    assertThat(view.isIndeterminate()).isTrue();
    action.accept(false);
    assertThat(view.isIndeterminate()).isFalse();
  }

  @Test @UiThreadTest public void max() throws Exception {
    Consumer<? super Integer> action = RxProgressBar.max(view);
    action.accept(100);
    assertThat(view.getMax()).isEqualTo(100);
    action.accept(1000);
    assertThat(view.getMax()).isEqualTo(1000);
  }

  @Test @UiThreadTest public void progress() throws Exception {
    Consumer<? super Integer> action = RxProgressBar.progress(view);
    assertThat(view.getProgress()).isEqualTo(0);
    action.accept(50);
    assertThat(view.getProgress()).isEqualTo(50);
    action.accept(100);
    assertThat(view.getProgress()).isEqualTo(100);
  }

  @Test @UiThreadTest public void secondaryProgress() throws Exception {
    Consumer<? super Integer> action = RxProgressBar.secondaryProgress(view);
    assertThat(view.getSecondaryProgress()).isEqualTo(0);
    action.accept(50);
    assertThat(view.getSecondaryProgress()).isEqualTo(50);
    action.accept(100);
    assertThat(view.getSecondaryProgress()).isEqualTo(100);
  }
}
