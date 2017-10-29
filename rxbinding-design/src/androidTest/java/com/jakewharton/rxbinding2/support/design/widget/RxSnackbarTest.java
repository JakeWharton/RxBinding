package com.jakewharton.rxbinding2.support.design.widget;

import android.app.Instrumentation;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.CountingIdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.FrameLayout;
import com.jakewharton.rxbinding2.RecordingObserver;
import com.jakewharton.rxbinding2.support.design.R;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static android.support.design.widget.Snackbar.Callback.DISMISS_EVENT_MANUAL;
import static android.support.design.widget.Snackbar.LENGTH_SHORT;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class) public final class RxSnackbarTest {
  private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
  private final Context rawContext = InstrumentationRegistry.getContext();
  private final Context context = new ContextThemeWrapper(rawContext, R.style.Theme_AppCompat);
  private final FrameLayout parent = new FrameLayout(context);
  @Rule public final ActivityTestRule<RxSnackbarTestActivity> activityRule =
          new ActivityTestRule<>(RxSnackbarTestActivity.class);
  private Snackbar snackbar;
  private CountingIdlingResource idler;

  @Before public void setUp() {
    RxSnackbarTestActivity activity = activityRule.getActivity();
    snackbar = activity.snackbar;

    idler = new CountingIdlingResource("counting idler");
    Espresso.registerIdlingResources(idler);
  }

  @After public void teardown() {
    Espresso.unregisterIdlingResources(idler);
  }

  @Test public void dismisses() {
    final Snackbar view = Snackbar.make(parent, "Hey", LENGTH_SHORT);

    RecordingObserver<Integer> o = new RecordingObserver<>();
    RxSnackbar.dismisses(view).subscribeOn(AndroidSchedulers.mainThread()).subscribe(o);
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.show();
      }
    });
    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.dismiss();
      }
    });
    assertEquals(DISMISS_EVENT_MANUAL, o.takeNext().intValue());

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.show();
      }
    });
    o.dispose();
    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.dismiss();
      }
    });
    o.assertNoMoreEvents();
  }

  @Test public void actionClicks() {
    Snackbar.Callback callback = new Snackbar.Callback() {
      @Override
      public void onShown(Snackbar sb) {
        idler.decrement();
      }
    };
    snackbar.addCallback(callback);

    final String actionText = "Action";

    RecordingObserver<View> o = new RecordingObserver<>();
    RxSnackbar.actionClicks(snackbar, actionText)
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(o);
    o.assertNoMoreEvents(); // No initial value.

    show();
    onView(withText(actionText)).perform(click());
    assertNotNull(o.takeNext());

    show();
    onView(withText(actionText)).perform(click());
    assertNotNull(o.takeNext());

    o.dispose();
    show();
    onView(withText(actionText)).perform(click());
    o.assertNoMoreEvents();

    snackbar.removeCallback(callback);
  }

  private void show() {
    idler.increment();
    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        snackbar.show();
      }
    });
    instrumentation.waitForIdleSync();
  }
}
