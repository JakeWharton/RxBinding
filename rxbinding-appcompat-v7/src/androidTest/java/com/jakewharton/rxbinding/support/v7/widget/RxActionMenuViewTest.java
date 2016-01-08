package com.jakewharton.rxbinding.support.v7.widget;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.ActionMenuView;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;

import com.jakewharton.rxbinding.RecordingObserver;
import com.jakewharton.rxbinding.support.v7.appcompat.R;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import rx.Subscription;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public final class RxActionMenuViewTest {

  @Rule public final UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();

  private final Context rawContext = InstrumentationRegistry.getContext();
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
    Subscription subscription = RxActionMenuView.itemClicks(view).subscribe(o);
    o.assertNoMoreEvents();

    menu.performIdentifierAction(2, 0);
    assertThat(o.takeNext()).isSameAs(item2);

    menu.performIdentifierAction(1, 0);
    assertThat(o.takeNext()).isSameAs(item1);

    subscription.unsubscribe();

    menu.performIdentifierAction(2, 0);
    o.assertNoMoreEvents();
  }
}
