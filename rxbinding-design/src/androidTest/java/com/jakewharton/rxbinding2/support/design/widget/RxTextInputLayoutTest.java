package com.jakewharton.rxbinding2.support.design.widget;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.ContextThemeWrapper;
import android.widget.EditText;
import com.jakewharton.rxbinding2.support.design.test.R;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public class RxTextInputLayoutTest {
  @Rule public final UiThreadTestRule uiThread = new UiThreadTestRule();

  private final Context rawContext = InstrumentationRegistry.getContext();
  private final Context context = new ContextThemeWrapper(rawContext, R.style.Theme_AppCompat);
  private final TextInputLayout view = new TextInputLayout(context);

  @Before public void setUp() {
    view.addView(new EditText(context));
  }

  @Test @UiThreadTest public void counterEnabled() throws Exception {
    RxTextInputLayout.counterEnabled(view).accept(true);
    assertThat(view.isCounterEnabled()).isEqualTo(true);
  }

  @Test @UiThreadTest public void counterMaxLength() throws Exception {
    RxTextInputLayout.counterMaxLength(view).accept(100);
    assertThat(view.getCounterMaxLength()).isEqualTo(100);
  }

  @Test @UiThreadTest public void error() throws Exception {
    RxTextInputLayout.error(view).accept("Your error here");
    assertThat(view.getError().toString()).isEqualTo("Your error here");
  }

  @Test @UiThreadTest public void errorRes() throws Exception {
    final String error = context.getString(R.string.error);
    RxTextInputLayout.errorRes(view).accept(R.string.error);
    assertThat(view.getError().toString()).isEqualTo(error);
  }

  @Test @UiThreadTest public void hint() throws Exception {
    RxTextInputLayout.hint(view).accept("Your name here");
    assertThat(view.getHint().toString()).isEqualTo("Your name here");
  }

  @Test @UiThreadTest public void hintRes() throws Exception {
    RxTextInputLayout.hintRes(view).accept(R.string.hint);
    assertThat(view.getHint().toString()).isEqualTo("Your name here");
  }
}
