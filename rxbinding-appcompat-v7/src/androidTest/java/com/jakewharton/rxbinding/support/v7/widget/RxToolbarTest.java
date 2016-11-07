package com.jakewharton.rxbinding.support.v7.widget;

import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.jakewharton.rxbinding.RecordingObserver;
import com.jakewharton.rxbinding.support.v7.appcompat.test.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static com.google.common.truth.Truth.assertThat;
import static com.jakewharton.rxbinding.support.v7.widget.RxToolbarTestActivity.NAVIGATION_CONTENT_DESCRIPTION;

public final class RxToolbarTest {
  @Rule public final ActivityTestRule<RxToolbarTestActivity> activityRule =
      new ActivityTestRule<>(RxToolbarTestActivity.class);

  private Toolbar view;

  @Before public void setUp() {
    RxToolbarTestActivity activity = activityRule.getActivity();
    view = activity.toolbar;
  }

  @Test @UiThreadTest public void itemClicks() {
    Menu menu = view.getMenu();
    MenuItem item1 = menu.add(0, 1, 0, "Hi");
    MenuItem item2 = menu.add(0, 2, 0, "Hey");

    RecordingObserver<MenuItem> o = new RecordingObserver<>();
    Subscription subscription = RxToolbar.itemClicks(view).subscribe(o);
    o.assertNoMoreEvents();

    menu.performIdentifierAction(2, 0);
    assertThat(o.takeNext()).isSameAs(item2);

    menu.performIdentifierAction(1, 0);
    assertThat(o.takeNext()).isSameAs(item1);

    subscription.unsubscribe();

    menu.performIdentifierAction(2, 0);
    o.assertNoMoreEvents();
  }

  @Test public void navigationClicks() {
    RecordingObserver<Void> o = new RecordingObserver<>();
    Subscription subscription = RxToolbar.navigationClicks(view)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents(); // No initial value.

    onView(withContentDescription(NAVIGATION_CONTENT_DESCRIPTION)).perform(click());
    assertThat(o.takeNext()).isNull();

    onView(withContentDescription(NAVIGATION_CONTENT_DESCRIPTION)).perform(click());
    assertThat(o.takeNext()).isNull();

    subscription.unsubscribe();

    onView(withContentDescription(NAVIGATION_CONTENT_DESCRIPTION)).perform(click());
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void title() {
    RxToolbar.title(view).call("Hey");
    assertThat(view.getTitle().toString()).isEqualTo("Hey");
  }

  @Test @UiThreadTest public void titleRes() {
    RxToolbar.titleRes(view).call(R.string.hey);
    assertThat(view.getTitle().toString()).isEqualTo("Hey");
  }

  @Test @UiThreadTest public void subtitle() {
    RxToolbar.subtitle(view).call("Hey");
    assertThat(view.getSubtitle().toString()).isEqualTo("Hey");
  }

  @Test @UiThreadTest public void subtitleRes() {
    RxToolbar.subtitleRes(view).call(R.string.hey);
    assertThat(view.getSubtitle().toString()).isEqualTo("Hey");
  }
}
