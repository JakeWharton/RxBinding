package com.jakewharton.rxbinding2.widget;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.jakewharton.rxbinding2.RecordingObserver;
import io.reactivex.functions.Consumer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@SuppressWarnings("ResourceType") // Don't need real IDs for test case.
public final class RxRadioGroupTest {
  @Rule public final UiThreadTestRule uiThread = new UiThreadTestRule();

  private final Context context = InstrumentationRegistry.getContext();
  private final RadioGroup view = new RadioGroup(context);

  @Before public void setUp() {
    RadioButton button1 = new RadioButton(context);
    button1.setId(1);
    view.addView(button1);
    RadioButton button2 = new RadioButton(context);
    button2.setId(2);
    view.addView(button2);
  }

  @Test @UiThreadTest public void checkedChanges() {
    RecordingObserver<Integer> o = new RecordingObserver<>();
    RxRadioGroup.checkedChanges(view).subscribe(o);
    assertEquals(-1, o.takeNext().intValue());

    view.check(1);
    assertEquals(1, o.takeNext().intValue());

    view.clearCheck();
    assertEquals(-1, o.takeNext().intValue());

    view.check(2);
    assertEquals(2, o.takeNext().intValue());

    o.dispose();

    view.check(1);
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void checked() throws Exception {
    Consumer<? super Integer> action = RxRadioGroup.checked(view);
    assertEquals(-1, view.getCheckedRadioButtonId());
    action.accept(1);
    assertEquals(1, view.getCheckedRadioButtonId());
    action.accept(-1);
    assertEquals(-1, view.getCheckedRadioButtonId());
    action.accept(2);
    assertEquals(2, view.getCheckedRadioButtonId());
  }
}
