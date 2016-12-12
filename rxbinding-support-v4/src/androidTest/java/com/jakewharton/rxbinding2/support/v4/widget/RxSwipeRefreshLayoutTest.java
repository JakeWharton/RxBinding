package com.jakewharton.rxbinding2.support.v4.widget;

import android.support.test.annotation.UiThreadTest;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.widget.SwipeRefreshLayout;
import com.jakewharton.rxbinding.support.v4.test.R;
import com.jakewharton.rxbinding2.RecordingObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class) public final class RxSwipeRefreshLayoutTest {
  @Rule public final ActivityTestRule<RxSwipeRefreshLayoutTestActivity> activityRule =
      new ActivityTestRule<>(RxSwipeRefreshLayoutTestActivity.class);

  private SwipeRefreshLayout view;

  @Before public void setUp() {
    RxSwipeRefreshLayoutTestActivity activity = activityRule.getActivity();
    view = activity.swipeRefreshLayout;
  }

  @Test public void refreshes() throws InterruptedException {
    RecordingObserver<Object> o = new RecordingObserver<>();
    RxSwipeRefreshLayout.refreshes(view)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents();

    onView(withId(R.id.swipe_refresh_layout)).perform(swipeDown());
    o.takeNext();

    o.dispose();
    onView(withId(R.id.swipe_refresh_layout)).perform(swipeDown());
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void refreshing() throws Exception {
    Consumer<? super Boolean> action = RxSwipeRefreshLayout.refreshing(view);
    assertThat(view.isRefreshing()).isFalse();

    action.accept(true);
    assertThat(view.isRefreshing()).isTrue();

    action.accept(false);
    assertThat(view.isRefreshing()).isFalse();
  }

  private static ViewAction swipeDown() {
    return new GeneralSwipeAction(Swipe.SLOW, GeneralLocation.TOP_CENTER,
        GeneralLocation.BOTTOM_CENTER, Press.FINGER);
  }
}
