package com.jakewharton.rxbinding.support.design.widget;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.ContextThemeWrapper;
import com.jakewharton.rxbinding.support.design.test.R;
import java.lang.reflect.Field;
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

  @Test @UiThreadTest public void counterEnabled()
      throws NoSuchFieldException, IllegalAccessException {
    RxTextInputLayout.counterEnabled(view).call(true);
    // TODO replace with getter once http://b.android.com/191175 is fixed.
    Field field = view.getClass().getDeclaredField("mCounterEnabled");
    field.setAccessible(true);
    assertThat(field.get(view)).isEqualTo(true);
  }

  @Test @UiThreadTest public void counterMaxLength() {
    RxTextInputLayout.counterMaxLength(view).call(100);
    assertThat(view.getCounterMaxLength()).isEqualTo(100);
  }

  @Test @UiThreadTest public void error() {
    RxTextInputLayout.error(view).call("Your error here");
    assertThat(view.getError().toString()).isEqualTo("Your error here");
  }

  @Test @UiThreadTest public void errorRes() {
    final String error = context.getString(R.string.error);
    RxTextInputLayout.errorRes(view).call(R.string.error);
    assertThat(view.getError().toString()).isEqualTo(error);
  }

  @Test @UiThreadTest public void hint() {
    RxTextInputLayout.hint(view).call("Your name here");
    assertThat(view.getHint().toString()).isEqualTo("Your name here");
  }

  @Test @UiThreadTest public void hintRes() {
    RxTextInputLayout.hintRes(view).call(R.string.hint);
    assertThat(view.getHint().toString()).isEqualTo("Your name here");
  }
}
