package com.jakewharton.rxbinding3.core;

import androidx.core.widget.NestedScrollView;
import androidx.test.annotation.UiThreadTest;
import androidx.test.rule.ActivityTestRule;
import com.jakewharton.rxbinding2.RecordingObserver;
import com.jakewharton.rxbinding2.view.ViewScrollChangeEvent;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

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
    assertSame(view, event.getView());
    assertEquals(1000, event.getScrollX());
    assertEquals(0, event.getScrollY());
    assertEquals(0, event.getOldScrollX());
    assertEquals(0, event.getOldScrollY());

    o.dispose();
    view.scrollTo(2000, 0);
    o.assertNoMoreEvents();
  }
}
