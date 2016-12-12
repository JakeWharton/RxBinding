package com.jakewharton.rxbinding2.widget;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.jakewharton.rxbinding2.RecordingObserver;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.functions.Consumer;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public final class RxCompoundButtonTest {
  @Rule public final UiThreadTestRule uiThread = new UiThreadTestRule();

  private final Context context = InstrumentationRegistry.getContext();
  private final CompoundButton view = new ToggleButton(context);

  @Test @UiThreadTest public void checkedChanges() {
    view.setChecked(false);

    RecordingObserver<Boolean> o = new RecordingObserver<>();
    RxCompoundButton.checkedChanges(view).subscribe(o);
    assertThat(o.takeNext()).isFalse();

    view.setChecked(true);
    assertThat(o.takeNext()).isTrue();
    view.setChecked(false);
    assertThat(o.takeNext()).isFalse();

    o.dispose();

    view.setChecked(true);
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void checked() throws Exception {
    view.setChecked(false);
    Consumer<? super Boolean> toggle = RxCompoundButton.checked(view);

    toggle.accept(true);
    assertThat(view.isChecked()).isTrue();

    toggle.accept(false);
    assertThat(view.isChecked()).isFalse();
  }

  @Test @UiThreadTest public void toggle() throws Exception {
    view.setChecked(false);
    Consumer<? super Object> toggle = RxCompoundButton.toggle(view);

    toggle.accept(null);
    assertThat(view.isChecked()).isTrue();

    toggle.accept("OMG TOGGLES");
    assertThat(view.isChecked()).isFalse();
  }
}
