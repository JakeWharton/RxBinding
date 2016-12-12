package com.jakewharton.rxbinding2.widget;

import android.annotation.TargetApi;
import android.support.test.annotation.UiThreadTest;
import android.support.test.filters.SdkSuppress;
import android.support.test.rule.ActivityTestRule;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;
import com.jakewharton.rxbinding.test.R;
import com.jakewharton.rxbinding2.RecordingObserver;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static com.google.common.truth.Truth.assertThat;
import static com.jakewharton.rxbinding2.widget.RxToolbarTestActivity.NAVIGATION_CONTENT_DESCRIPTION;

@TargetApi(LOLLIPOP)
@SdkSuppress(minSdkVersion = LOLLIPOP)
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
    RxToolbar.itemClicks(view).subscribe(o);
    o.assertNoMoreEvents();

    menu.performIdentifierAction(2, 0);
    assertThat(o.takeNext()).isSameAs(item2);

    menu.performIdentifierAction(1, 0);
    assertThat(o.takeNext()).isSameAs(item1);

    o.dispose();

    menu.performIdentifierAction(2, 0);
    o.assertNoMoreEvents();
  }

  @Test public void navigationClicks() {
    RecordingObserver<Object> o = new RecordingObserver<>();
    RxToolbar.navigationClicks(view)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents(); // No initial value.

    onView(withContentDescription(NAVIGATION_CONTENT_DESCRIPTION)).perform(click());
    assertThat(o.takeNext()).isNotNull();

    onView(withContentDescription(NAVIGATION_CONTENT_DESCRIPTION)).perform(click());
    assertThat(o.takeNext()).isNotNull();

    o.dispose();

    onView(withContentDescription(NAVIGATION_CONTENT_DESCRIPTION)).perform(click());
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void title() throws Exception {
    RxToolbar.title(view).accept("Hey");
    assertThat(view.getTitle().toString()).isEqualTo("Hey");
  }

  @Test @UiThreadTest public void titleRes() throws Exception {
    RxToolbar.titleRes(view).accept(R.string.hey);
    assertThat(view.getTitle().toString()).isEqualTo("Hey");
  }

  @Test @UiThreadTest public void subtitle() throws Exception {
    RxToolbar.subtitle(view).accept("Hey");
    assertThat(view.getSubtitle().toString()).isEqualTo("Hey");
  }

  @Test @UiThreadTest public void subtitleRes() throws Exception {
    RxToolbar.subtitleRes(view).accept(R.string.hey);
    assertThat(view.getSubtitle().toString()).isEqualTo("Hey");
  }
}
