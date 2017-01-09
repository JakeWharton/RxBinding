package com.jakewharton.rxbinding2.support.design.widget;

import android.support.design.widget.Snackbar;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.jakewharton.rxbinding2.RecordingObserver;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static android.support.design.widget.Snackbar.Callback.DISMISS_EVENT_MANUAL;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class) public final class RxSnackbarTest {
  @Rule public final ActivityTestRule<RxSnackbarTestActivity> activityRule =
        new ActivityTestRule<>(RxSnackbarTestActivity.class);
  private Snackbar view;

  @Before public void setUp() {
    RxSnackbarTestActivity activity = activityRule.getActivity();
    view = activity.snackbar;
  }

  @Test public void dismisses() {
    RecordingObserver<Integer> o = new RecordingObserver<>();
    RxSnackbar.dismisses(view).subscribeOn(AndroidSchedulers.mainThread()).subscribe(o);
    o.assertNoMoreEvents();

    view.show();
    view.dismiss();
    assertEquals(DISMISS_EVENT_MANUAL, o.takeNext().intValue());

    view.show();
    o.dispose();
    view.dismiss();
    o.assertNoMoreEvents();
  }

  @Test public void actionIsClicked() {
    String actionText = "Action";

    RecordingObserver<View> o = new RecordingObserver<>();
    RxSnackbar.actionClicked(view, actionText).subscribeOn(AndroidSchedulers.mainThread()).subscribe(o);
    o.assertNoMoreEvents();

    view.show();
    onView(withText(actionText)).perform(customClick());
    assertNotNull(o.takeNext());

    view.show();
    o.dispose();
    onView(withText(actionText)).perform(customClick());
    o.assertNoMoreEvents();
  }

  private static ViewAction customClick() {
    return new ViewAction() {
      @Override public Matcher<View> getConstraints() {
        return isEnabled();
      }

      @Override public String getDescription() {
        return "custom click action";
      }

      @Override public void perform(UiController uiController, View view) {
        view.performClick();
      }
    };
  }
}
