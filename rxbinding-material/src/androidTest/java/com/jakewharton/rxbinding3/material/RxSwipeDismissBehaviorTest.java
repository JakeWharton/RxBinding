package com.jakewharton.rxbinding3.material;

import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Swipe;
import androidx.test.rule.ActivityTestRule;
import com.google.android.material.behavior.SwipeDismissBehavior;
import com.jakewharton.rxbinding2.RecordingObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

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
    assertEquals(view, o.takeNext());

    o.dispose();

    onView(withId(1)).perform(swipeRight());
    o.assertNoMoreEvents();
  }

  private static ViewAction swipeRight() {
    return new GeneralSwipeAction(Swipe.FAST, GeneralLocation.CENTER_LEFT,
        GeneralLocation.CENTER_RIGHT, Press.FINGER);
  }
}
