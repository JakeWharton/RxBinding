package com.jakewharton.rxbinding2.support.design.widget;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import com.jakewharton.rxbinding2.RecordingObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public final class RxSwipeDismissBehaviorTest {
  @Rule public final ActivityTestRule<RxSwipeDismissBehaviorTestActivity> activityRule =
      new ActivityTestRule<>(RxSwipeDismissBehaviorTestActivity.class);

  private View view;

  @Before public void setUp() {
    RxSwipeDismissBehaviorTestActivity activity = activityRule.getActivity();
    view = activity.view;
  }

  @Test public void dismisses() {
    ((CoordinatorLayout.LayoutParams) view.getLayoutParams()).setBehavior(
        new SwipeDismissBehavior());

    RecordingObserver<View> o = new RecordingObserver<>();
    RxSwipeDismissBehavior.dismisses(view)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents(); // No initial value.

    onView(withId(1)).perform(swipeRight());
    assertThat(o.takeNext()).isEqualTo(view);

    o.dispose();

    onView(withId(1)).perform(swipeRight());
    o.assertNoMoreEvents();
  }

  private static ViewAction swipeRight() {
    return new GeneralSwipeAction(Swipe.FAST, GeneralLocation.CENTER_LEFT,
        GeneralLocation.CENTER_RIGHT, Press.FINGER);
  }
}
