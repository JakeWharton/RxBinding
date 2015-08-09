package com.jakewharton.rxbinding.widget;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextSwitcher;
import android.widget.TextView;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import rx.functions.Action1;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public final class RxTextSwitcherTest {
  @Rule public final UiThreadTestRule uiThread = new UiThreadTestRule();

  private final Context context = InstrumentationRegistry.getContext();
  private final TextSwitcher view = new TextSwitcher(context);
  private final TextView textView1 = new TextView(context);
  private final TextView textView2 = new TextView(context);

  @Before public void setUp() {
    view.addView(textView1);
    view.addView(textView2);
  }

  @Test @UiThreadTest public void text() {
    Action1<? super CharSequence> action = RxTextSwitcher.text(view);
    action.call("Hey");
    assertThat(textView2.getText().toString()).isEqualTo("Hey");
    action.call("Hello");
    assertThat(textView1.getText().toString()).isEqualTo("Hello");
    action.call("Hi");
    assertThat(textView2.getText().toString()).isEqualTo("Hi");
  }

  @Test @UiThreadTest public void currentText() {
    Action1<? super CharSequence> action = RxTextSwitcher.currentText(view);
    action.call("Hey");
    assertThat(textView1.getText().toString()).isEqualTo("Hey");
    action.call("Hello");
    assertThat(textView1.getText().toString()).isEqualTo("Hello");
    action.call("Hi");
    assertThat(textView1.getText().toString()).isEqualTo("Hi");
  }
}
