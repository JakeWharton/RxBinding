package com.jakewharton.rxbinding3.swiperefreshlayout;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Swipe;
import androidx.test.rule.ActivityTestRule;
import com.jakewharton.rxbinding3.swiperefreshlayout.test.R;
import com.jakewharton.rxbinding2.RecordingObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public final class RxSwipeRefreshLayoutTest {
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

  private static ViewAction swipeDown() {
    return new GeneralSwipeAction(Swipe.SLOW, GeneralLocation.TOP_CENTER,
        GeneralLocation.BOTTOM_CENTER, Press.FINGER);
  }
}
