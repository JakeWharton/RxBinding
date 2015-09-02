package com.jakewharton.rxbinding.view;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.UiThreadTest;
import android.view.View;
import android.widget.LinearLayout;
import com.jakewharton.rxbinding.RecordingObserver;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public final class RxViewGroupTest {
  @Rule public final ActivityTestRule<RxViewGroupTestActivity> activityRule =
      new ActivityTestRule<>(RxViewGroupTestActivity.class);

  private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
  private LinearLayout viewGroup;
  private View child;

  @Before public void setUp() {
    RxViewGroupTestActivity activity = activityRule.getActivity();
    viewGroup = activity.viewGroup;
    child = activity.child;
  }

  @Test public void childViewEvents() {
    RecordingObserver<ViewGroupHierarchyChangeEvent> o = new RecordingObserver<>();
    Subscription subscription = RxViewGroup.changeEvents(viewGroup)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents(); // No initial value.

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        viewGroup.addView(child);
      }
    });
    assertThat(o.takeNext()).isEqualTo(ViewGroupHierarchyChildViewAddEvent.create(viewGroup, child));

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        viewGroup.removeView(child);
      }
    });
    assertThat(o.takeNext()).isEqualTo(ViewGroupHierarchyChildViewRemoveEvent.create(viewGroup, child));

    subscription.unsubscribe();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        viewGroup.addView(child);
      }
    });
    o.assertNoMoreEvents();
  }
}
