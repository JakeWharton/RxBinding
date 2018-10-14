package com.jakewharton.rxbinding3.material;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import androidx.test.InstrumentationRegistry;
import androidx.test.annotation.UiThreadTest;
import com.google.android.material.navigation.NavigationView;
import com.jakewharton.rxbinding2.RecordingObserver;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public final class RxNavigationViewTest {
  private final Context rawContext = InstrumentationRegistry.getContext();
  private final Context context = new ContextThemeWrapper(rawContext, R.style.Theme_AppCompat);
  private final NavigationView view = new NavigationView(context);
  private final Menu menu = view.getMenu();
  private final MenuItem item1 = menu.add(1, 1, 0, "Hi");
  private final MenuItem item2 = menu.add(1, 2, 0, "Hey");

  @Before public void setUp() {
    menu.setGroupCheckable(1, true, true);
  }

  @Test @UiThreadTest public void itemSelections() {
    RecordingObserver<MenuItem> o = new RecordingObserver<>();
    RxNavigationView.itemSelections(view).subscribe(o);
    o.assertNoMoreEvents();

    menu.performIdentifierAction(2, 0);
    assertSame(item2, o.takeNext());

    menu.performIdentifierAction(1, 0);
    assertSame(item1, o.takeNext());

    o.dispose();

    menu.performIdentifierAction(2, 0);
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void itemSelectionsInitialValue() {
    item2.setChecked(true);

    RecordingObserver<MenuItem> o = new RecordingObserver<>();
    RxNavigationView.itemSelections(view).subscribe(o);
    assertSame(item2, o.takeNext());
  }
}
