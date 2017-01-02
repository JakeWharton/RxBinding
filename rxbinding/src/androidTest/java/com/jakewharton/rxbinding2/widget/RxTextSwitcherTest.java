package com.jakewharton.rxbinding2.widget;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextSwitcher;
import android.widget.TextView;
import io.reactivex.functions.Consumer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

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

  @Test @UiThreadTest public void text() throws Exception {
    Consumer<? super CharSequence> action = RxTextSwitcher.text(view);
    action.accept("Hey");
    assertEquals("Hey", textView2.getText().toString());
    action.accept("Hello");
    assertEquals("Hello", textView1.getText().toString());
    action.accept("Hi");
    assertEquals("Hi", textView2.getText().toString());
  }

  @Test @UiThreadTest public void currentText() throws Exception {
    Consumer<? super CharSequence> action = RxTextSwitcher.currentText(view);
    action.accept("Hey");
    assertEquals("Hey", textView1.getText().toString());
    action.accept("Hello");
    assertEquals("Hello", textView1.getText().toString());
    action.accept("Hi");
    assertEquals("Hi", textView1.getText().toString());
  }
}
