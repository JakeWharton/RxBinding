package com.jakewharton.rxbinding.widget;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.jakewharton.rxbinding.RecordingObserver;
import com.jakewharton.rxbinding.view.ActivityResultEvent;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class RxActivityResultTest {

  private Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
  private RxActivityResultTestActivity activity;

  @Rule
  public final UiThreadTestRule uiThread = new UiThreadTestRule();

  @Rule
  public final ActivityTestRule<RxActivityResultTestActivity> activityRule =
      new ActivityTestRule<RxActivityResultTestActivity>(RxActivityResultTestActivity.class) {

        @Override
        protected Intent getActivityIntent() {
          return RxActivityResultTestActivity.createActivityStartIntent(instrumentation.getTargetContext(), "TestActivity");
        }
      };

  @Before
  public void setup() {
    activity = activityRule.getActivity();
  }

  @Test
  public void startActivityForResult() throws Exception {
    // GIVEN an Activity (TestActivity) launches an Activity (ResultActivity)
    onView(withText("TestActivity")).check(matches(isDisplayed()));
    final RecordingObserver<ActivityResultEvent> o = new RecordingObserver<>();

    // AND observes the result
    final Intent intent = RxActivityResultTestActivity.createActivityStartIntent(activity, "ResultActivity");
    RxActivityResult.startActivityForResult(activity, intent)
        .subscribe(o);

    // WHEN the launched (ResultActivity) finishes
    onView(withText("ResultActivity")).perform(click());
    onView(withText("TestActivity")).check(matches(isDisplayed()));

    // AND result was observed
    ActivityResultEvent resultEvent = o.takeNextWait();
    o.assertOnCompleted();
    assertNotNull(resultEvent);
    assertEquals(Activity.RESULT_OK, resultEvent.getResultCode());
    assertNotNull(resultEvent.getData());
    assertEquals("value", resultEvent.getData().getStringExtra("key"));

    // THEN the headless fragment was removed from the host Activity (TestActivity)
    instrumentation.runOnMainSync(new Runnable() {
      @Override
      public void run() {
        activity.getFragmentManager().executePendingTransactions();
        assertNull(activity.getFragmentManager().findFragmentByTag(ActivityResultBroker.TAG));
      }
    });

  }

}