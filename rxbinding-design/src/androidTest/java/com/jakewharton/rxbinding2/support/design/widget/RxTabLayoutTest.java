package com.jakewharton.rxbinding2.support.design.widget;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.ContextThemeWrapper;
import com.jakewharton.rxbinding2.RecordingObserver;
import com.jakewharton.rxbinding2.support.design.R;
import io.reactivex.functions.Consumer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public final class RxTabLayoutTest {
  @Rule public final UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();

  private final Context rawContext = InstrumentationRegistry.getContext();
  private final Context context = new ContextThemeWrapper(rawContext, R.style.Theme_AppCompat);
  private final TabLayout view = new TabLayout(context);
  private final TabLayout.Tab tab1 = view.newTab();
  private final TabLayout.Tab tab2 = view.newTab();

  @Before public void setUp() {
    view.addTab(tab1);
    view.addTab(tab2);
  }

  @Test @UiThreadTest public void selectionEvents() {
    RecordingObserver<TabLayoutSelectionEvent> o = new RecordingObserver<>();
    RxTabLayout.selectionEvents(view).subscribe(o);
    assertEquals(TabLayoutSelectionSelectedEvent.create(view, tab1), o.takeNext());

    tab2.select();
    assertEquals(TabLayoutSelectionUnselectedEvent.create(view, tab1), o.takeNext());
    assertEquals(TabLayoutSelectionSelectedEvent.create(view, tab2), o.takeNext());

    tab2.select(); // Reselection
    assertEquals(TabLayoutSelectionReselectedEvent.create(view, tab2), o.takeNext());

    tab1.select();
    assertEquals(TabLayoutSelectionUnselectedEvent.create(view, tab2), o.takeNext());
    assertEquals(TabLayoutSelectionSelectedEvent.create(view, tab1), o.takeNext());

    o.dispose();

    tab2.select();
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void selectionEventsNoInitial() {
    TabLayout empty = new TabLayout(context);

    RecordingObserver<TabLayoutSelectionEvent> o = new RecordingObserver<>();
    RxTabLayout.selectionEvents(empty).subscribe(o);
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void selections() {
    RecordingObserver<TabLayout.Tab> o = new RecordingObserver<>();
    RxTabLayout.selections(view).subscribe(o);
    assertSame(tab1, o.takeNext());

    tab2.select();
    assertSame(tab2, o.takeNext());

    tab2.select(); // Reselection
    o.assertNoMoreEvents();

    tab1.select();
    assertSame(tab1, o.takeNext());

    o.dispose();

    tab2.select();
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void selectionsNoInitial() {
    TabLayout empty = new TabLayout(context);

    RecordingObserver<TabLayout.Tab> o = new RecordingObserver<>();
    RxTabLayout.selections(empty).subscribe(o);
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void select() throws Exception {
    Consumer<? super Integer> action = RxTabLayout.select(view);
    assertEquals(0, view.getSelectedTabPosition());
    action.accept(1);
    assertEquals(1, view.getSelectedTabPosition());
    action.accept(0);
    assertEquals(0, view.getSelectedTabPosition());
  }

  @Test @UiThreadTest public void selectInvalidValueThrows() throws Exception {
    Consumer<? super Integer> action = RxTabLayout.select(view);
    try {
      action.accept(2);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("No tab for index 2", e.getMessage());
    }
    try {
      action.accept(-1);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals("No tab for index -1", e.getMessage());
    }
  }
}
