package com.jakewharton.rxbinding2.widget;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ListView;
import android.widget.Spinner;
import com.jakewharton.rxbinding2.RecordingObserver;
import com.jakewharton.rxbinding2.UnsafeRunnable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

@RunWith(AndroidJUnit4.class)
public final class RxAdapterViewTest {
  @Rule public final ActivityTestRule<RxAdapterViewTestActivity> activityRule =
      new ActivityTestRule<>(RxAdapterViewTestActivity.class);

  private Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

  private RxAdapterViewTestActivity activity;
  private Spinner spinner;
  private ListView listView;

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

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        spinner.setSelection(2);
      }
    });
    assertEquals(2, o.takeNext().intValue());

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        spinner.setSelection(0);
      }
    });
    assertEquals(0, o.takeNext().intValue());

    o.dispose();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        spinner.setSelection(1);
      }
    });
    o.assertNoMoreEvents();
  }

  @Test public void selectionEvents() {
    RecordingObserver<AdapterViewSelectionEvent> o = new RecordingObserver<>();
    RxAdapterView.selectionEvents(spinner)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    AdapterViewItemSelectionEvent event1 = (AdapterViewItemSelectionEvent) o.takeNext();
    assertSame(spinner, event1.view());
    assertNotNull(event1.selectedView());
    assertEquals(0, event1.position());
    assertEquals(0, event1.id());

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        spinner.setSelection(2);
      }
    });
    AdapterViewItemSelectionEvent event2 = (AdapterViewItemSelectionEvent) o.takeNext();
    assertSame(spinner, event2.view());
    assertNotNull(event2.selectedView());
    assertEquals(2, event2.position());
    assertEquals(2, event2.id());

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        activity.values.clear();
        activity.adapter.notifyDataSetChanged();
      }
    });
    assertEquals(AdapterViewNothingSelectionEvent.create(spinner), o.takeNext());

    o.dispose();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        activity.values.add("Hello");
        activity.adapter.notifyDataSetChanged();
      }
    });
    o.assertNoMoreEvents();
  }

  @Test public void selection() {
    final Consumer<? super Integer> action = RxAdapterView.selection(spinner);

    instrumentation.runOnMainSync(new UnsafeRunnable() {
      @Override public void unsafeRun() throws Exception {
        action.accept(2);
      }
    });
    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        assertEquals(2, spinner.getSelectedItemPosition());
      }
    });

    instrumentation.runOnMainSync(new UnsafeRunnable() {
      @Override public void unsafeRun() throws Exception {
        action.accept(1);
      }
    });
    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        assertEquals(1, spinner.getSelectedItemPosition());
      }
    });
  }

  @Test public void itemClicks() {
    RecordingObserver<Integer> o = new RecordingObserver<>();
    RxAdapterView.itemClicks(listView)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        listView.performItemClick(listView.getChildAt(2), 2, 2);
      }
    });
    assertEquals(2, o.takeNext().intValue());

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        listView.performItemClick(listView.getChildAt(0), 0, 0);
      }
    });
    assertEquals(0, o.takeNext().intValue());

    o.dispose();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        listView.performItemClick(listView.getChildAt(1), 1, 1);
      }
    });
    o.assertNoMoreEvents();
  }

  @Test public void itemClickEvents() {
    RecordingObserver<AdapterViewItemClickEvent> o = new RecordingObserver<>();
    RxAdapterView.itemClickEvents(listView)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        listView.performItemClick(listView.getChildAt(2), 2, 2);
      }
    });
    AdapterViewItemClickEvent event = o.takeNext();
    assertEquals(listView, event.view());
    assertNotNull(event.clickedView());
    assertEquals(2, event.position());
    assertEquals(2, event.id());

    o.dispose();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        listView.performItemClick(listView.getChildAt(1), 1, 1);
      }
    });
    o.assertNoMoreEvents();
  }

  @Test public void itemLongClicks() {
    // TODO
  }

  @Test public void itemLongClickEvents() {
    // TODO
  }
}
