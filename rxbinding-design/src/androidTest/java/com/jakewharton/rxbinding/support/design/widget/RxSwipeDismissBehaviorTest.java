package com.jakewharton.rxbinding.support.design.widget;

import android.app.Instrumentation;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import com.jakewharton.rxbinding.RecordingObserver;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;
import static com.google.common.truth.Truth.assertThat;
import static com.jakewharton.rxbinding.MotionEventUtil.motionEventAtPosition;

@RunWith(AndroidJUnit4.class)
public final class RxSwipeDismissBehaviorTest {
  @Rule public final ActivityTestRule<RxSwipeDismissBehaviorTestActivity> activityRule =
      new ActivityTestRule<>(RxSwipeDismissBehaviorTestActivity.class);

  private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
  private View view;

  @Before public void setUp() {
    RxSwipeDismissBehaviorTestActivity activity = activityRule.getActivity();
    view = activity.view;
  }

  @Test public void dismisses() {
    ((CoordinatorLayout.LayoutParams) view.getLayoutParams()).setBehavior(
        new SwipeDismissBehavior());

    RecordingObserver<View> o = new RecordingObserver<>();
    Subscription subscription = RxSwipeDismissBehavior.dismisses(view)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents(); // No initial value.

    doSwipeGesture(view);
    assertThat(o.takeNext()).isEqualTo(view);

    subscription.unsubscribe();

    doSwipeGesture(view);
    o.assertNoMoreEvents();
  }

  private void doSwipeGesture(View view) {
    instrumentation.sendPointerSync(motionEventAtPosition(view, ACTION_DOWN, 50, 0));
    instrumentation.sendPointerSync(motionEventAtPosition(view, ACTION_MOVE, 500, 0));
    instrumentation.sendPointerSync(motionEventAtPosition(view, ACTION_UP, 500, 0));
  }
}
