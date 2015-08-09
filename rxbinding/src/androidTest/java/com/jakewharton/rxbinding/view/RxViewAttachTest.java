package com.jakewharton.rxbinding.view;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.FrameLayout;

import com.jakewharton.rxbinding.RecordingObserver;
import com.jakewharton.rxbinding.ViewDirtyIdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static com.google.common.truth.Truth.assertThat;
import static com.jakewharton.rxbinding.view.ViewAttachEvent.Kind.ATTACH;
import static com.jakewharton.rxbinding.view.ViewAttachEvent.Kind.DETACH;

@RunWith(AndroidJUnit4.class)
public final class RxViewAttachTest {
  @Rule public final ActivityTestRule<RxViewAttachTestActivity> activityRule =
      new ActivityTestRule<>(RxViewAttachTestActivity.class);

  private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
  private FrameLayout parent;
  private View child;
  private ViewDirtyIdlingResource viewDirtyIdler;

  @Before public void setUp() {
    RxViewAttachTestActivity activity = activityRule.getActivity();
    parent = activity.parent;
    child = activity.child;
    viewDirtyIdler = new ViewDirtyIdlingResource(activity);
    Espresso.registerIdlingResources(viewDirtyIdler);
  }

  @After public void tearDown() {
    Espresso.unregisterIdlingResources(viewDirtyIdler);
  }

  @Test public void attachEvents() {
    RecordingObserver<ViewAttachEvent> o = new RecordingObserver<>();
    Subscription subscription = RxView.attachEvents(child)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents(); // No initial value.

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        parent.addView(child);
      }
    });
    instrumentation.waitForIdleSync();
    assertThat(o.takeNext().kind()).isEqualTo(ATTACH);
    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        parent.removeView(child);
      }
    });
    instrumentation.waitForIdleSync();
    assertThat(o.takeNext().kind()).isEqualTo(DETACH);

    subscription.unsubscribe();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        parent.addView(child);
        parent.removeView(child);
      }
    });
    instrumentation.waitForIdleSync();
    o.assertNoMoreEvents();
  }
}
