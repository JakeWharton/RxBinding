package com.jakewharton.rxbinding4.widget;

import android.app.Instrumentation;
import android.widget.ListView;
import android.widget.Spinner;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.jakewharton.rxbinding4.RecordingObserver;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public final class RxAdapterViewTest {
  @Rule public final ActivityTestRule<RxAdapterViewTestActivity> activityRule =
      new ActivityTestRule<>(RxAdapterViewTestActivity.class);

  private Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

  RxAdapterViewTestActivity activity;
  Spinner spinner;
  ListView listView;

  @Before public void setUp() {
    activity = activityRule.getActivity();
    spinner = activity.spinner;
    listView = activity.listView;
  }

  @Test public void itemSelections() {
    RecordingObserver<Integer> o = new RecordingObserver<>();
    RxAdapterView.itemSelections(spinner)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    assertEquals(0, o.takeNext().intValue());

    instrumentation.runOnMainSync(() -> spinner.setSelection(2));
    assertEquals(2, o.takeNext().intValue());

    instrumentation.runOnMainSync(() -> spinner.setSelection(0));
    assertEquals(0, o.takeNext().intValue());

    o.dispose();

    instrumentation.runOnMainSync(() -> spinner.setSelection(1));
    o.assertNoMoreEvents();
  }

  @Test public void selectionEvents() {
    RecordingObserver<AdapterViewSelectionEvent> o = new RecordingObserver<>();
    RxAdapterView.selectionEvents(spinner)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    AdapterViewItemSelectionEvent event1 = (AdapterViewItemSelectionEvent) o.takeNext();
    assertSame(spinner, event1.getView());
    assertNotNull(event1.getSelectedView());
    assertEquals(0, event1.getPosition());
    assertEquals(0, event1.getId());

    instrumentation.runOnMainSync(() -> spinner.setSelection(2));
    AdapterViewItemSelectionEvent event2 = (AdapterViewItemSelectionEvent) o.takeNext();
    assertSame(spinner, event2.getView());
    assertNotNull(event2.getSelectedView());
    assertEquals(2, event2.getPosition());
    assertEquals(2, event2.getId());

    instrumentation.runOnMainSync(() -> {
      activity.values.clear();
      activity.adapter.notifyDataSetChanged();
    });
    assertEquals(new AdapterViewNothingSelectionEvent(spinner), o.takeNext());

    o.dispose();

    instrumentation.runOnMainSync(() -> {
      activity.values.add("Hello");
      activity.adapter.notifyDataSetChanged();
    });
    o.assertNoMoreEvents();
  }

  @Test public void itemClicks() {
    RecordingObserver<Integer> o = new RecordingObserver<>();
    RxAdapterView.itemClicks(listView)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(() -> listView.performItemClick(listView.getChildAt(2), 2, 2));
    assertEquals(2, o.takeNext().intValue());

    instrumentation.runOnMainSync(() -> listView.performItemClick(listView.getChildAt(0), 0, 0));
    assertEquals(0, o.takeNext().intValue());

    o.dispose();

    instrumentation.runOnMainSync(() -> listView.performItemClick(listView.getChildAt(1), 1, 1));
    o.assertNoMoreEvents();
  }

  @Test public void itemClickEvents() {
    RecordingObserver<AdapterViewItemClickEvent> o = new RecordingObserver<>();
    RxAdapterView.itemClickEvents(listView)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(() -> listView.performItemClick(listView.getChildAt(2), 2, 2));
    AdapterViewItemClickEvent event = o.takeNext();
    assertEquals(listView, event.getView());
    assertNotNull(event.getClickedView());
    assertEquals(2, event.getPosition());
    assertEquals(2, event.getId());

    o.dispose();

    instrumentation.runOnMainSync(() -> listView.performItemClick(listView.getChildAt(1), 1, 1));
    o.assertNoMoreEvents();
  }

  @Test public void itemLongClicks() {
    // TODO
  }

  @Test public void itemLongClickEvents() {
    // TODO
  }
}
