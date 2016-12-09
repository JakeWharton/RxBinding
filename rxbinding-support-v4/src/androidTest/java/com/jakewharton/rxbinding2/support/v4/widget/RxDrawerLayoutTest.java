package com.jakewharton.rxbinding2.support.v4.widget;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.widget.DrawerLayout;
import com.jakewharton.rxbinding.ViewDirtyIdlingResource;
import com.jakewharton.rxbinding2.RecordingObserver;
import com.jakewharton.rxbinding2.UnsafeRunnable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.view.Gravity.RIGHT;
import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public final class RxDrawerLayoutTest {
  @Rule public final ActivityTestRule<RxDrawerLayoutTestActivity> activityRule =
      new ActivityTestRule<>(RxDrawerLayoutTestActivity.class);

  private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

  private DrawerLayout view;
  private ViewDirtyIdlingResource viewDirtyIdler;

  @Before public void setUp() {
    RxDrawerLayoutTestActivity activity = activityRule.getActivity();
    view = activity.drawerLayout;

    viewDirtyIdler = new ViewDirtyIdlingResource(activity);
    Espresso.registerIdlingResources(viewDirtyIdler);
  }

  @After public void tearDown() {
    Espresso.unregisterIdlingResources(viewDirtyIdler);
  }

  @Test public void drawerOpen() {
    RecordingObserver<Boolean> o = new RecordingObserver<>();
    RxDrawerLayout.drawerOpen(view, RIGHT) //
        .subscribeOn(AndroidSchedulers.mainThread()) //
        .subscribe(o);
    assertThat(o.takeNext()).isFalse();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.openDrawer(RIGHT);
      }
    });
    assertThat(o.takeNext()).isTrue();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.closeDrawer(RIGHT);
      }
    });
    assertThat(o.takeNext()).isFalse();

    o.dispose();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.openDrawer(RIGHT);
      }
    });
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
