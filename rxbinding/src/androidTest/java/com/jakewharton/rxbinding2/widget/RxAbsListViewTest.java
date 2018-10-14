package com.jakewharton.rxbinding2.widget;

import android.app.Instrumentation;
import android.widget.ListView;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.jakewharton.rxbinding2.RecordingObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

public final class RxAbsListViewTest {
  @Rule public final ActivityTestRule<RxAbsListViewTestActivity> activityRule =
      new ActivityTestRule<>(RxAbsListViewTestActivity.class);

  private Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

  private RxAbsListViewTestActivity activity;
  ListView listView;

  @Before public void setUp() {
    activity = activityRule.getActivity();
    listView = activity.listView;
  }

  @Test public void scrollEvents() {
    RecordingObserver<AbsListViewScrollEvent> o = new RecordingObserver<>();
    RxAbsListView.scrollEvents(listView)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    AbsListViewScrollEvent event = o.takeNext();
    assertEquals(100, event.getTotalItemCount());
    assertEquals(0, event.getFirstVisibleItem());
    assertEquals(0, event.getScrollState());

    instrumentation.runOnMainSync(() -> listView.smoothScrollToPosition(50));
    AbsListViewScrollEvent event1 = o.takeNext();
    assertEquals(listView, event1.getView());
    assertEquals(100, event1.getTotalItemCount());

    o.dispose();

    instrumentation.runOnMainSync(() -> listView.smoothScrollToPosition(100));
    o.assertNoMoreEvents();
  }
}
