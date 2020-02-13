package com.jakewharton.rxbinding3.material;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import androidx.test.annotation.UiThreadTest;
import androidx.test.core.app.ApplicationProvider;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jakewharton.rxbinding3.RecordingObserver;
import com.jakewharton.rxbinding3.material.test.R;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class RxBottomNavigationViewTest {
  private final Context rawContext = ApplicationProvider.getApplicationContext();
  private final Context context = new ContextThemeWrapper(rawContext, R.style.Theme_AppCompat);
  private final BottomNavigationView view = new BottomNavigationView(context);
  private final Menu menu = view.getMenu();

  @Before public void setUp() {
    view.inflateMenu(R.menu.menu);
  }

  @After public void teardown() {
    menu.clear();
  }

  @Test @UiThreadTest public void itemSelections() {
    RecordingObserver<MenuItem> o = new RecordingObserver<>();
    RxBottomNavigationView.itemSelections(view).subscribe(o);

    // initial value
    assertEquals(R.id.menu_item_one, o.takeNext().getItemId());

    menu.performIdentifierAction(R.id.menu_item_two, 0);
    assertEquals(R.id.menu_item_two, o.takeNext().getItemId());

    menu.performIdentifierAction(R.id.menu_item_one, 0);
    assertEquals(R.id.menu_item_one, o.takeNext().getItemId());

    o.dispose();

    menu.performIdentifierAction(R.id.menu_item_two, 0);
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void itemReselections() {
    RecordingObserver<MenuItem> o = new RecordingObserver<>();
    RxBottomNavigationView.itemReselections(view).subscribe(o);

    menu.performIdentifierAction(R.id.menu_item_one, 0);
    assertEquals(R.id.menu_item_one, o.takeNext().getItemId());

    menu.performIdentifierAction(R.id.menu_item_one, 0);
    assertEquals(R.id.menu_item_one, o.takeNext().getItemId());

    o.dispose();

    menu.performIdentifierAction(R.id.menu_item_one, 0);
    o.assertNoMoreEvents();
  }
}
