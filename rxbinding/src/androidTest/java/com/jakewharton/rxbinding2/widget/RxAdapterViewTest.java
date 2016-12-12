package com.jakewharton.rxbinding2.widget;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ListView;
import android.widget.Spinner;
import com.jakewharton.rxbinding2.RecordingObserver;
import com.jakewharton.rxbinding2.UnsafeRunnable;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

import static com.google.common.truth.Truth.assertThat;

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
    assertThat(o.takeNext()).isEqualTo(0);

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        spinner.setSelection(2);
      }
    });
    assertThat(o.takeNext()).isEqualTo(2);

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        spinner.setSelection(0);
      }
    });
    assertThat(o.takeNext()).isEqualTo(0);

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
    assertThat(event1.view()).isSameAs(spinner);
    assertThat(event1.selectedView()).isNotNull();
    assertThat(event1.position()).isEqualTo(0);
    assertThat(event1.id()).isEqualTo(0);

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        spinner.setSelection(2);
      }
    });
    AdapterViewItemSelectionEvent event2 = (AdapterViewItemSelectionEvent) o.takeNext();
    assertThat(event2.view()).isSameAs(spinner);
    assertThat(event2.selectedView()).isNotNull();
    assertThat(event2.position()).isEqualTo(2);
    assertThat(event2.id()).isEqualTo(2);

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        activity.values.clear();
        activity.adapter.notifyDataSetChanged();
      }
    });
    assertThat(o.takeNext()).isEqualTo(AdapterViewNothingSelectionEvent.create(spinner));

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
        assertThat(spinner.getSelectedItemPosition()).isEqualTo(2);
      }
    });

    instrumentation.runOnMainSync(new UnsafeRunnable() {
      @Override public void unsafeRun() throws Exception {
        action.accept(1);
      }
    });
    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        assertThat(spinner.getSelectedItemPosition()).isEqualTo(1);
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
    assertThat(o.takeNext()).isEqualTo(2);

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        listView.performItemClick(listView.getChildAt(0), 0, 0);
      }
    });
    assertThat(o.takeNext()).isEqualTo(0);

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
    assertThat(event.view()).isEqualTo(listView);
    assertThat(event.clickedView()).isNotNull();
    assertThat(event.position()).isEqualTo(2);
    assertThat(event.id()).isEqualTo(2);

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
