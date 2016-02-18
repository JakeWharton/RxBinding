package com.jakewharton.rxbinding.widget;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ListView;
import com.jakewharton.rxbinding.RecordingObserver;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public final class RxAbsListViewTest {
  @Rule public final ActivityTestRule<RxAbsListViewTestActivity> activityRule =
      new ActivityTestRule<>(RxAbsListViewTestActivity.class);

  private Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

  private RxAbsListViewTestActivity activity;
  private ListView listView;

  @Before public void setUp() {
    activity = activityRule.getActivity();
    listView = activity.listView;
  }

  @Test public void scrollEvents() {
    RecordingObserver<AbsListViewScrollEvent> o = new RecordingObserver<>();
    Subscription subscription = RxAbsListView.scrollEvents(listView) //
        .subscribeOn(AndroidSchedulers.mainThread()) //
        .subscribe(o);
    AbsListViewScrollEvent event = o.takeNext();
    assertThat(event.totalItemCount()).isEqualTo(100);
    assertThat(event.firstVisibleItem()).isEqualTo(0);
    assertThat(event.scrollState()).isEqualTo(0);

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        listView.smoothScrollToPosition(50);
      }
    });
    AbsListViewScrollEvent event1 = o.takeNext();
    assertThat(event1.view()).isEqualTo(listView);
    assertThat(event1.totalItemCount()).isEqualTo(100);

    subscription.unsubscribe();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        listView.smoothScrollToPosition(100);
      }
    });
    o.assertNoMoreEvents();
  }
}
