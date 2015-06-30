package com.jakewharton.rxbinding.widget;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import rx.Subscription;
import com.jakewharton.rxbinding.RecordingObserver;
import rx.functions.Action1;

import static com.google.common.truth.Truth.assertThat;

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
    Subscription subscription = RxRadioGroup.checkedChanges(view)
        .distinctUntilChanged() // Radio group changes fire twice!
        .subscribe(o);
    o.assertNoMoreEvents();

    view.check(1);
    assertThat(o.takeNext()).isEqualTo(1);

    view.clearCheck();
    assertThat(o.takeNext()).isEqualTo(-1);

    view.check(2);
    assertThat(o.takeNext()).isEqualTo(2);

    subscription.unsubscribe();

    view.check(1);
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void checkedChangeEvents() {
    RecordingObserver<RadioGroupCheckedChangeEvent> o = new RecordingObserver<>();
    Subscription subscription = RxRadioGroup.checkedChangeEvents(view)
        .distinctUntilChanged() // Radio group changes fire twice!
        .subscribe(o);
    o.assertNoMoreEvents();

    view.check(1);
    assertThat(o.takeNext()).isEqualTo(RadioGroupCheckedChangeEvent.create(view, 1));

    view.clearCheck();
    assertThat(o.takeNext()).isEqualTo(RadioGroupCheckedChangeEvent.create(view, -1));

    view.check(2);
    assertThat(o.takeNext()).isEqualTo(RadioGroupCheckedChangeEvent.create(view, 2));

    subscription.unsubscribe();

    view.check(1);
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void checked() {
    Action1<? super Integer> action = RxRadioGroup.checked(view);
    assertThat(view.getCheckedRadioButtonId()).isEqualTo(-1);
    action.call(1);
    assertThat(view.getCheckedRadioButtonId()).isEqualTo(1);
    action.call(-1);
    assertThat(view.getCheckedRadioButtonId()).isEqualTo(-1);
    action.call(2);
    assertThat(view.getCheckedRadioButtonId()).isEqualTo(2);
  }
}
