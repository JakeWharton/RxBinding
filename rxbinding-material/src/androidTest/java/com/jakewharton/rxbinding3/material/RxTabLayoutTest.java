package com.jakewharton.rxbinding3.material;

import android.content.Context;
import android.view.ContextThemeWrapper;
import androidx.test.annotation.UiThreadTest;
import androidx.test.core.app.ApplicationProvider;
import com.google.android.material.tabs.TabLayout;
import com.jakewharton.rxbinding3.RecordingObserver;
import io.reactivex.functions.Consumer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

public final class RxTabLayoutTest {
  private final Context rawContext = ApplicationProvider.getApplicationContext();
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
    assertEquals(new TabLayoutSelectionSelectedEvent(view, tab1), o.takeNext());

    tab2.select();
    assertEquals(new TabLayoutSelectionUnselectedEvent(view, tab1), o.takeNext());
    assertEquals(new TabLayoutSelectionSelectedEvent(view, tab2), o.takeNext());

    tab2.select(); // Reselection
    assertEquals(new TabLayoutSelectionReselectedEvent(view, tab2), o.takeNext());

    tab1.select();
    assertEquals(new TabLayoutSelectionUnselectedEvent(view, tab2), o.takeNext());
    assertEquals(new TabLayoutSelectionSelectedEvent(view, tab1), o.takeNext());

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

  @Test @UiThreadTest public void select() throws Throwable {
    Consumer<? super Integer> action = RxTabLayout.select(view);
    assertEquals(0, view.getSelectedTabPosition());
    action.accept(1);
    assertEquals(1, view.getSelectedTabPosition());
    action.accept(0);
    assertEquals(0, view.getSelectedTabPosition());
  }

  @Test @UiThreadTest public void selectInvalidValueThrows() throws Throwable {
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
