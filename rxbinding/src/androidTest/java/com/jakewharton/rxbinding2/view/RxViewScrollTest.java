package com.jakewharton.rxbinding2.view;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.jakewharton.rxbinding2.RecordingObserver;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@RunWith(AndroidJUnit4.class)
public final class RxViewScrollTest {
  @Rule public final ActivityTestRule<RxViewScrollTestActivity> activityRule =
          new ActivityTestRule<>(RxViewScrollTestActivity.class);

  private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
  private View view;

  @Before
  public void setUp() {
    RxViewScrollTestActivity activity = activityRule.getActivity();
    view = activity.view;
  }

  @SdkSuppress(minSdkVersion = 19)
  @Test public void scrollChangeEvents() {
    RecordingObserver<ViewScrollChangeEvent> o = new RecordingObserver<>();
    RxView.scrollChangeEvents(view)
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(o);
    o.assertNoMoreEvents();


    instrumentation.runOnMainSync(
            new Runnable() {
              @Override public void run() {
                view.scrollTo(1, 1);
              }
            });
    ViewScrollChangeEvent event0 = o.takeNext();
    assertSame(view, event0.view());
    assertEquals(1, event0.scrollX());
    assertEquals(1, event0.scrollY());
    assertEquals(0, event0.oldScrollX());
    assertEquals(0, event0.oldScrollY());

    instrumentation.runOnMainSync(
            new Runnable() {
              @Override public void run() {
                view.scrollTo(2, 2);
              }
            });
    ViewScrollChangeEvent event1 = o.takeNext();
    assertSame(view, event1.view());
    assertEquals(2, event1.scrollX());
    assertEquals(2, event1.scrollY());
    assertEquals(1, event1.oldScrollX());
    assertEquals(1, event1.oldScrollY());

    o.dispose();
    instrumentation.runOnMainSync(
            new Runnable() {
              @Override public void run() {
                view.scrollTo(3, 3);
              }
            });
    o.assertNoMoreEvents();
  }
}
