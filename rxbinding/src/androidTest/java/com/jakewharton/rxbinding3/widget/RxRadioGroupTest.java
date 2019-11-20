package com.jakewharton.rxbinding3.widget;

import android.content.Context;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.test.annotation.UiThreadTest;
import androidx.test.core.app.ApplicationProvider;
import com.jakewharton.rxbinding3.RecordingObserver;
import io.reactivex.functions.Consumer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("ResourceType") // Don't need real IDs for test case.
public final class RxRadioGroupTest {
  private final Context context = ApplicationProvider.getApplicationContext();
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
