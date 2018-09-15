package com.jakewharton.rxbinding2.support.design.chip;

import android.content.Context;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.view.ContextThemeWrapper;

import com.jakewharton.rxbinding2.RecordingObserver;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.functions.Consumer;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@SuppressWarnings("ResourceType") // Don't need real IDs for test case.
public final class RxChipGroupTest {
  @Rule public final UiThreadTestRule uiThread = new UiThreadTestRule();

  private final Context context = new ContextThemeWrapper(InstrumentationRegistry.getContext(), R.style.Theme_AppCompat);
  private final ChipGroup view = new ChipGroup(context);

  @Before public void setUp() {
    view.setSingleSelection(true);
    Chip button1 = new Chip(context);
    button1.setId(1);
    view.addView(button1);
    Chip button2 = new Chip(context);
    button2.setId(2);
    view.addView(button2);
  }

  @Test @UiThreadTest public void checkedChanges() {
    RecordingObserver<Integer> o = new RecordingObserver<>();
    RxChipGroup.checkedChanges(view).subscribe(o);
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
    Consumer<? super Integer> action = RxChipGroup.checked(view);
    assertEquals(-1, view.getCheckedChipId());
    action.accept(1);
    assertEquals(1, view.getCheckedChipId());
    action.accept(-1);
    assertEquals(-1, view.getCheckedChipId());
    action.accept(2);
    assertEquals(2, view.getCheckedChipId());
  }
}
