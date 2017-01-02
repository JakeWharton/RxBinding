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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

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
    assertSame(view, event.view());
    assertEquals(1000, event.scrollX());
    assertEquals(0, event.scrollY());
    assertEquals(0, event.oldScrollX());
    assertEquals(0, event.oldScrollY());

    o.dispose();
    view.scrollTo(2000, 0);
    o.assertNoMoreEvents();
  }
}
