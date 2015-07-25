package com.jakewharton.rxbinding.support.v4.widget;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import com.jakewharton.rxbinding.RecordingObserver;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

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

  @Before public void setUp() throws NoSuchFieldException {
    RxDrawerLayoutTestActivity activity = activityRule.getActivity();
    view = activity.drawerLayout;

    final View decorView = activity.getWindow().getDecorView();
    Espresso.registerIdlingResources(new IdlingResource() {
      private ResourceCallback resourceCallback;

      @Override public String getName() {
        return "view dirty";
      }

      @Override public boolean isIdleNow() {
        boolean clean = !decorView.isDirty();
        if (clean) {
          resourceCallback.onTransitionToIdle();
        }
        return clean;
      }

      @Override public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
      }
    });
  }

  @Test public void drawerOpen() {
    RecordingObserver<Boolean> o = new RecordingObserver<>();
    Subscription subscription = RxDrawerLayout.drawerOpen(view) //
        .subscribeOn(AndroidSchedulers.mainThread()) //
        .subscribe(o);

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

    subscription.unsubscribe();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.openDrawer(RIGHT);
      }
    });
    o.assertNoMoreEvents();
  }

  @Test public void open() {
    final Action1<? super Boolean> open = RxDrawerLayout.open(view, RIGHT);

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        open.call(true);
      }
    });
    onView(withId(view.getId())).check(matches(isOpen(RIGHT)));

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        open.call(false);
      }
    });
    onView(withId(view.getId())).check(matches(isClosed(RIGHT)));
  }
}
