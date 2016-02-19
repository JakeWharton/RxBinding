package com.jakewharton.rxbinding.support.v4.widget;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.widget.SwipeRefreshLayout;
import com.jakewharton.rxbinding.RecordingObserver;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;
import static com.google.common.truth.Truth.assertThat;
import static com.jakewharton.rxbinding.MotionEventUtil.motionEventAtPosition;

@RunWith(AndroidJUnit4.class)
public final class RxSwipeRefreshLayoutTest {
  @Rule public final ActivityTestRule<RxSwipeRefreshLayoutTestActivity> activityRule =
      new ActivityTestRule<>(RxSwipeRefreshLayoutTestActivity.class);

  private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

  private SwipeRefreshLayout view;

  @Before public void setUp() {
    RxSwipeRefreshLayoutTestActivity activity = activityRule.getActivity();
    view = activity.swipeRefreshLayout;
  }

  @Ignore("https://github.com/JakeWharton/RxBinding/issues/72")
  @Test public void refreshes() {
    RecordingObserver<Void> o = new RecordingObserver<>();
    Subscription subscription = RxSwipeRefreshLayout.refreshes(view)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents();

    doRefreshGesture();
    o.takeNext();

    subscription.unsubscribe();
    doRefreshGesture();
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void refreshing() {
    Action1<? super Boolean> action = RxSwipeRefreshLayout.refreshing(view);
    assertThat(view.isRefreshing()).isFalse();

    action.call(true);
    assertThat(view.isRefreshing()).isTrue();

    action.call(false);
    assertThat(view.isRefreshing()).isFalse();
  }

  private void doRefreshGesture() {
    instrumentation.sendPointerSync(motionEventAtPosition(view, ACTION_DOWN, 50, 0));
    instrumentation.sendPointerSync(motionEventAtPosition(view, ACTION_MOVE, 50, 100));
    instrumentation.sendPointerSync(motionEventAtPosition(view, ACTION_UP, 50, 100));
  }
}
