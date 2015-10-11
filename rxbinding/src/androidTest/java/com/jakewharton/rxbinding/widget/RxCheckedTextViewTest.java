package com.jakewharton.rxbinding.widget;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.CheckedTextView;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public class RxCheckedTextViewTest {
  @Rule public final UiThreadTestRule uiThread = new UiThreadTestRule();

  private final Context context = InstrumentationRegistry.getContext();
  private final CheckedTextView view = new CheckedTextView(context);

  @Test @UiThreadTest public void check() {
    view.setChecked(false);
    RxCheckedTextView.check(view).call(true);
    assertThat(view.isChecked()).isEqualTo(true);
  }
}
