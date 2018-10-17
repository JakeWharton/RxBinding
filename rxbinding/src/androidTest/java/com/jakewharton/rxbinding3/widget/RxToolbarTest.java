package com.jakewharton.rxbinding3.widget;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;
import androidx.test.annotation.UiThreadTest;
import androidx.test.filters.SdkSuppress;
import androidx.test.rule.ActivityTestRule;
import com.jakewharton.rxbinding3.RecordingObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static com.jakewharton.rxbinding3.widget.RxToolbarTestActivity.NAVIGATION_CONTENT_DESCRIPTION;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

@SdkSuppress(minSdkVersion = 21)
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
    assertSame(item2, o.takeNext());

    menu.performIdentifierAction(1, 0);
    assertSame(item1, o.takeNext());

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
    assertNotNull(o.takeNext());

    onView(withContentDescription(NAVIGATION_CONTENT_DESCRIPTION)).perform(click());
    assertNotNull(o.takeNext());

    o.dispose();

    onView(withContentDescription(NAVIGATION_CONTENT_DESCRIPTION)).perform(click());
    o.assertNoMoreEvents();
  }
}
