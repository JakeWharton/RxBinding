package com.jakewharton.rxbinding2.support.design.widget;

import android.content.Context;
import android.support.design.widget.BottomNavigationView;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import com.jakewharton.rxbinding2.RecordingObserver;
import com.jakewharton.rxbinding2.support.design.test.R;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class) public final class RxBottomNavigationViewTest {
  @Rule public final UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();

  private final Context rawContext = InstrumentationRegistry.getContext();
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
    assertThat(o.takeNext().getItemId()).isEqualTo(R.id.menu_item_one);

    menu.performIdentifierAction(R.id.menu_item_two, 0);
    assertThat(o.takeNext().getItemId()).isEqualTo(R.id.menu_item_two);

    menu.performIdentifierAction(R.id.menu_item_one, 0);
    assertThat(o.takeNext().getItemId()).isEqualTo(R.id.menu_item_one);

    o.dispose();

    menu.performIdentifierAction(R.id.menu_item_two, 0);
    o.assertNoMoreEvents();
  }
}
