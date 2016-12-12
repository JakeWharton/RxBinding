package com.jakewharton.rxbinding2.support.v4.widget;

import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.widget.NestedScrollView;
import com.jakewharton.rxbinding2.RecordingObserver;
import com.jakewharton.rxbinding2.view.ViewScrollChangeEvent;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public final class RxNestedScrollViewTest {
  @Rule public final ActivityTestRule<RxNestedScrollViewTestActivity> activityRule =
      new ActivityTestRule<>(RxNestedScrollViewTestActivity.class);

  private NestedScrollView view;

  @Before public void setUp() {
    RxNestedScrollViewTestActivity activity = activityRule.getActivity();
    view = activity.nestedScrollView;
  }

  @Test @UiThreadTest public void scrollChangeEvents() {
    RecordingObserver<ViewScrollChangeEvent> o = new RecordingObserver<>();
    RxNestedScrollView.scrollChangeEvents(view).subscribe(o);
    o.assertNoMoreEvents();

    view.scrollTo(1000, 0);
    ViewScrollChangeEvent event = o.takeNext();
    assertThat(event.view()).isSameAs(view);
    assertThat(event.scrollX()).isEqualTo(1000);
    assertThat(event.scrollY()).isEqualTo(0);
    assertThat(event.oldScrollX()).isEqualTo(0);
    assertThat(event.oldScrollY()).isEqualTo(0);

    o.dispose();
    view.scrollTo(2000, 0);
    o.assertNoMoreEvents();
  }
}
