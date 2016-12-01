package com.jakewharton.rxbinding.support.design.widget;

import android.support.design.widget.Snackbar;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.jakewharton.rxbinding.RecordingObserver;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static android.support.design.widget.Snackbar.Callback.DISMISS_EVENT_MANUAL;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public final class RxSnackbarTest {
  @Rule public final ActivityTestRule<RxSnackbarTestActivity> activityRule =
        new ActivityTestRule<>(RxSnackbarTestActivity.class);

  private Snackbar view;

  @Before public void setUp() {
    RxSnackbarTestActivity activity = activityRule.getActivity();
    view = activity.snackbar;
  }

  @Test public void dismisses() {

    RecordingObserver<Integer> o = new RecordingObserver<>();
    Subscription subscription = RxSnackbar.dismisses(view)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents();

    view.show();
    view.dismiss();
    assertThat(o.takeNext()).isEqualTo(DISMISS_EVENT_MANUAL);

    view.show();
    subscription.unsubscribe();
    view.dismiss();
    o.assertNoMoreEvents();
  }

  @Test public void actionIsClicked() {
    String actionText = "Action";

    RecordingObserver<Integer> o = new RecordingObserver<>();
    RxSnackbar.actionClicked(view, actionText)
          .subscribeOn(AndroidSchedulers.mainThread())
          .subscribe(o);
    o.assertNoMoreEvents();

    view.show();
    onView(withText(actionText)).perform(click());
    o.takeNext();
    o.assertNoMoreEvents();
  }

  @Test public void actionIsClickedButNotSubscribed() {
    String actionText = "Action";

    RecordingObserver<Integer> o = new RecordingObserver<>();
    Subscription subscription = RxSnackbar.actionClicked(view, actionText)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents();

    view.show();
    subscription.unsubscribe();
    onView(withText(actionText)).perform(click());
    o.assertNoMoreEvents();
  }
}
