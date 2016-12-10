package com.jakewharton.rxbinding2.support.v4.widget;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.CountingIdlingResource;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.View;
import com.jakewharton.rxbinding2.RecordingObserver;
import com.jakewharton.rxbinding2.UnsafeRunnable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class) public class RxSlidingPaneLayoutTest {
  @Rule public final ActivityTestRule<RxSlidingPaneLayoutTestActivity> activityRule =
      new ActivityTestRule<>(RxSlidingPaneLayoutTestActivity.class);

  private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

  private SlidingPaneLayout view;

  private CountingIdlingResource idler;

  @Before public void setUp() {
    RxSlidingPaneLayoutTestActivity activity = activityRule.getActivity();
    view = activity.slidingPaneLayout;

    idler = new CountingIdlingResource("counting idler");
    Espresso.registerIdlingResources(idler);
  }

  @After public void teardown() {
    Espresso.unregisterIdlingResources(idler);
  }

  @Test public void paneOpen() {
    RecordingObserver<Boolean> o = new RecordingObserver<>();
    RxSlidingPaneLayout.panelOpens(view)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    assertThat(o.takeNext()).isFalse();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.openPane();
      }
    });
    assertThat(o.takeNext()).isTrue();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.closePane();
      }
    });
    assertThat(o.takeNext()).isFalse();

    o.dispose();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.openPane();
      }
    });
    o.assertNoMoreEvents();
  }

  @Test public void slides() {
    RecordingObserver<Float> o1 = new RecordingObserver<>();
    RxSlidingPaneLayout.panelSlides(view)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o1);
    o1.assertNoMoreEvents();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.openPane();
      }
    });
    instrumentation.waitForIdleSync();
    assertThat(o1.takeNext()).isGreaterThan(0f);

    o1.dispose();
    o1.assertNoMoreEvents();

    RecordingObserver<Float> o2 = new RecordingObserver<>();
    RxSlidingPaneLayout.panelSlides(view)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o2);
    o2.assertNoMoreEvents();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.closePane();
      }
    });
    instrumentation.waitForIdleSync();
    assertThat(o2.takeNext()).isLessThan(1.0f);

    o2.dispose();
    o2.assertNoMoreEvents();
  }

  @Test public void open() {
    final Consumer<? super Boolean> open = RxSlidingPaneLayout.open(view);

    view.setPanelSlideListener(new SlidingPaneLayout.SimplePanelSlideListener() {
      @Override public void onPanelOpened(View panel) {
        idler.decrement();
      }

      @Override public void onPanelClosed(View panel) {
        idler.decrement();
      }
    });

    idler.increment();
    instrumentation.runOnMainSync(new UnsafeRunnable() {
      @Override protected void unsafeRun() throws Exception {
        open.accept(true);
      }
    });
    instrumentation.waitForIdleSync();
    onView(withId(view.getId())).check(matches(isOpen()));

    idler.increment();
    instrumentation.runOnMainSync(new UnsafeRunnable() {
      @Override protected void unsafeRun() throws Exception {
        open.accept(false);
      }
    });
    instrumentation.waitForIdleSync();
    onView(withId(view.getId())).check(matches(isClosed()));

    view.setPanelSlideListener(null);
  }

  private static Matcher<View> isOpen() {
    return new BoundedMatcher<View, SlidingPaneLayout>(SlidingPaneLayout.class) {
      @Override public void describeTo(Description description) {
        description.appendText("is pane open");
      }

      @Override public boolean matchesSafely(SlidingPaneLayout slidingPaneLayout) {
        return slidingPaneLayout.isOpen();
      }
    };
  }

  private static Matcher<View> isClosed() {
    return new BoundedMatcher<View, SlidingPaneLayout>(SlidingPaneLayout.class) {
      @Override public void describeTo(Description description) {
        description.appendText("is pane closed");
      }

      @Override public boolean matchesSafely(SlidingPaneLayout slidingPaneLayout) {
        return !slidingPaneLayout.isOpen();
      }
    };
  }
}
