package com.jakewharton.rxbinding.support.v4.view;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.ViewPager;
import com.jakewharton.rxbinding.RecordingObserver;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import rx.Subscription;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public final class RxViewPagerTest {
  @Rule public final ActivityTestRule<RxViewPagerTestActivity> activityRule =
      new ActivityTestRule<>(RxViewPagerTestActivity.class);

  private ViewPager view;

  @Before public void setUp() {
    RxViewPagerTestActivity activity = activityRule.getActivity();
    view = activity.viewPager;
  }

  @Test @UiThreadTest public void pageSelections() {
    view.setCurrentItem(0);
    RecordingObserver<Integer> o = new RecordingObserver<>();
    Subscription subscription = RxViewPager.pageSelections(view).subscribe(o);
    assertThat(o.takeNext()).isEqualTo(0);

    view.setCurrentItem(3);
    assertThat(o.takeNext()).isEqualTo(3);
    view.setCurrentItem(5);
    assertThat(o.takeNext()).isEqualTo(5);

    subscription.unsubscribe();

    view.setCurrentItem(0);
    o.assertNoMoreEvents();
  }
}
