package com.jakewharton.rxbinding.support.design.widget;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.ContextThemeWrapper;
import com.jakewharton.rxbinding.support.design.test.R;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public class RxTextInputLayoutTest {
  @Rule public final UiThreadTestRule uiThread = new UiThreadTestRule();

  private final Context rawContext = InstrumentationRegistry.getContext();
  private final Context context = new ContextThemeWrapper(rawContext, com.jakewharton.rxbinding.support.design.R.style.Theme_AppCompat);
  private final TextInputLayout view = new TextInputLayout(context);

  @Test @UiThreadTest public void hint() {
    RxTextInputLayout.hint(view).call("Your name here");
    assertThat(view.getHint().toString()).isEqualTo("Your name here");
  }

  @Test @UiThreadTest public void hintRes() {
    RxTextInputLayout.hintRes(view).call(R.string.hint);
    assertThat(view.getHint().toString()).isEqualTo("Your name here");
  }
}
