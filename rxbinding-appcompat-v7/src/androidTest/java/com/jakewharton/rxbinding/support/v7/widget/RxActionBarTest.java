package com.jakewharton.rxbinding.support.v7.widget;

import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.app.ActionBar;
import com.jakewharton.rxbinding.RecordingObserver;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import rx.Subscription;

import static com.google.common.truth.Truth.assertThat;

public final class RxActionBarTest {
  @Rule public final ActivityTestRule<RxActionBarTestActivity> activityRule =
      new ActivityTestRule<>(RxActionBarTestActivity.class);

  private ActionBar view;

  @Before public void setUp() {
    RxActionBarTestActivity activity = activityRule.getActivity();
    view = activity.actionBar;
  }

  @Test @UiThreadTest public void menuVisibilityChanges() {
    RecordingObserver<Boolean> o = new RecordingObserver<>();
    Subscription subscription = RxActionBar.menuVisibilityChanges(view).subscribe(o);
    o.assertNoMoreEvents();

    view.dispatchMenuVisibilityChanged(true);
    assertThat(o.takeNext()).isEqualTo(true);

    view.dispatchMenuVisibilityChanged(false);
    assertThat(o.takeNext()).isEqualTo(false);

    subscription.unsubscribe();

    view.dispatchMenuVisibilityChanged(true);
    o.assertNoMoreEvents();
  }
}
