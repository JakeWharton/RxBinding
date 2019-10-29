package com.jakewharton.rxbinding3.drawerlayout;

import android.app.Instrumentation;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.jakewharton.rxbinding3.ViewDirtyIdlingResource;
import com.jakewharton.rxbinding3.RecordingObserver;
import com.jakewharton.rxbinding3.UnsafeRunnable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.view.Gravity.RIGHT;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.contrib.DrawerMatchers.isOpen;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class RxDrawerLayoutTest {
  @Rule public final ActivityTestRule<RxDrawerLayoutTestActivity> activityRule =
      new ActivityTestRule<>(RxDrawerLayoutTestActivity.class);

  private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
  private final IdlingRegistry idlingRegistry = IdlingRegistry.getInstance();

  DrawerLayout view;
  private ViewDirtyIdlingResource viewDirtyIdler;

  @Before public void setUp() {
    RxDrawerLayoutTestActivity activity = activityRule.getActivity();
    view = activity.drawerLayout;

    viewDirtyIdler = new ViewDirtyIdlingResource(activity);
    idlingRegistry.register(viewDirtyIdler);
  }

  @After public void tearDown() {
    idlingRegistry.unregister(viewDirtyIdler);
  }

  @Test public void drawerOpen() {
    RecordingObserver<Boolean> o = new RecordingObserver<>();
    RxDrawerLayout.drawerOpen(view, RIGHT) //
        .subscribeOn(AndroidSchedulers.mainThread()) //
        .subscribe(o);
    assertFalse(o.takeNext());

    instrumentation.runOnMainSync(() -> view.openDrawer(RIGHT));
    assertTrue(o.takeNext());

    instrumentation.runOnMainSync(() -> view.closeDrawer(RIGHT));
    assertFalse(o.takeNext());

    o.dispose();

    instrumentation.runOnMainSync(() -> view.openDrawer(RIGHT));
    o.assertNoMoreEvents();
  }

  @Test public void open() {
    final Consumer<? super Boolean> open = RxDrawerLayout.open(view, RIGHT);

    instrumentation.runOnMainSync(new UnsafeRunnable() {

      @Override protected void unsafeRun() throws Exception {
        open.accept(true);
      }
    });
    onView(withId(view.getId())).check(matches(isOpen(RIGHT)));

    instrumentation.runOnMainSync(new UnsafeRunnable() {
      @Override protected void unsafeRun() throws Exception {
        open.accept(false);
      }
    });
    onView(withId(view.getId())).check(matches(isClosed(RIGHT)));
  }
}
