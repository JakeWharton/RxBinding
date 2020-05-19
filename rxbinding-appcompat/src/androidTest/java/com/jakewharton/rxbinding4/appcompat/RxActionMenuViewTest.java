package com.jakewharton.rxbinding4.appcompat;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.ActionMenuView;
import androidx.test.annotation.UiThreadTest;
import androidx.test.core.app.ApplicationProvider;
import com.jakewharton.rxbinding4.RecordingObserver;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public final class RxActionMenuViewTest {
  private final Context rawContext = ApplicationProvider.getApplicationContext();
  private final Context context = new ContextThemeWrapper(rawContext, R.style.Theme_AppCompat);

  private ActionMenuView view;

  @Before public void setUp() {
    view = new ActionMenuView(context);
  }

  @Test @UiThreadTest public void itemClicks() {
    Menu menu = view.getMenu();

    MenuItem item1 = menu.add(0, 1, 0, "Hi");
    MenuItem item2 = menu.add(0, 2, 0, "Hey");

    RecordingObserver<MenuItem> o = new RecordingObserver<>();
    RxActionMenuView.itemClicks(view).subscribe(o);
    o.assertNoMoreEvents();

    menu.performIdentifierAction(2, 0);
    assertSame(item2, o.takeNext());

    menu.performIdentifierAction(1, 0);
    assertSame(item1, o.takeNext());

    o.dispose();

    menu.performIdentifierAction(2, 0);
    o.assertNoMoreEvents();
  }
}
