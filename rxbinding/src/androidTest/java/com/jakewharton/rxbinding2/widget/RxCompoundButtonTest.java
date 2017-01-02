package com.jakewharton.rxbinding2.widget;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import com.jakewharton.rxbinding2.RecordingObserver;
import io.reactivex.functions.Consumer;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public final class RxCompoundButtonTest {
  @Rule public final UiThreadTestRule uiThread = new UiThreadTestRule();

  private final Context context = InstrumentationRegistry.getContext();
  private final CompoundButton view = new ToggleButton(context);

  @Test @UiThreadTest public void checkedChanges() {
    view.setChecked(false);

    RecordingObserver<Boolean> o = new RecordingObserver<>();
    RxCompoundButton.checkedChanges(view).subscribe(o);
    assertFalse(o.takeNext());

    view.setChecked(true);
    assertTrue(o.takeNext());
    view.setChecked(false);
    assertFalse(o.takeNext());

    o.dispose();

    view.setChecked(true);
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void checked() throws Exception {
    view.setChecked(false);
    Consumer<? super Boolean> toggle = RxCompoundButton.checked(view);

    toggle.accept(true);
    assertTrue(view.isChecked());

    toggle.accept(false);
    assertFalse(view.isChecked());
  }

  @Test @UiThreadTest public void toggle() throws Exception {
    view.setChecked(false);
    Consumer<? super Object> toggle = RxCompoundButton.toggle(view);

    toggle.accept(null);
    assertTrue(view.isChecked());

    toggle.accept("OMG TOGGLES");
    assertFalse(view.isChecked());
  }
}
