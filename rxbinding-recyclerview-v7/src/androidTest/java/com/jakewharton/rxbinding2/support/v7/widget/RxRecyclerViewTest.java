package com.jakewharton.rxbinding2.support.v7.widget;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jakewharton.rxbinding.ViewDirtyIdlingResource;
import com.jakewharton.rxbinding2.RecordingObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public final class RxRecyclerViewTest {
  @Rule public final ActivityTestRule<RxRecyclerViewTestActivity> activityRule =
      new ActivityTestRule<>(RxRecyclerViewTestActivity.class);

  private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

  RecyclerView view;
  private ViewDirtyIdlingResource viewDirtyIdler;
  private View child;

  @Before public void setUp() {
    RxRecyclerViewTestActivity activity = activityRule.getActivity();
    view = activity.recyclerView;
    child = new View(activityRule.getActivity());
    viewDirtyIdler = new ViewDirtyIdlingResource(activity);
    Espresso.registerIdlingResources(viewDirtyIdler);
  }

  @After public void tearDown() {
    Espresso.unregisterIdlingResources(viewDirtyIdler);
  }

  @Test public void childAttachEvents() {
    RecordingObserver<RecyclerViewChildAttachStateChangeEvent> o = new RecordingObserver<>();
    RxRecyclerView.childAttachStateChangeEvents(view)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents();

    final SimpleAdapter adapter = new SimpleAdapter(child);

    instrumentation.runOnMainSync(() -> view.setAdapter(adapter));
    assertEquals(RecyclerViewChildAttachEvent.create(view, child), o.takeNext());

    o.dispose();

    instrumentation.runOnMainSync(() -> view.setAdapter(adapter));

    o.assertNoMoreEvents();
  }

  @Test public void childDetachEvents() {
    final SimpleAdapter adapter = new SimpleAdapter(child);

    instrumentation.runOnMainSync(() -> view.setAdapter(adapter));

    RecordingObserver<RecyclerViewChildAttachStateChangeEvent> o = new RecordingObserver<>();
    RxRecyclerView.childAttachStateChangeEvents(view)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(() -> view.setAdapter(null));
    assertEquals(RecyclerViewChildDetachEvent.create(view, child), o.takeNext());

    o.dispose();

    instrumentation.runOnMainSync(() -> view.setAdapter(adapter));

    o.assertNoMoreEvents();
  }

  @Test public void scrollEventsVertical() {
    instrumentation.runOnMainSync(() -> view.setAdapter(new Adapter()));

    RecordingObserver<RecyclerViewScrollEvent> o = new RecordingObserver<>();
    RxRecyclerView.scrollEvents(view)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(() -> view.scrollBy(0, 50));
    RecyclerViewScrollEvent event1 = o.takeNext();
    assertNotNull(event1);
    assertEquals(50, event1.dy());

    instrumentation.runOnMainSync(() -> view.scrollBy(0, 0));
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(() -> view.scrollBy(0, -50));
    RecyclerViewScrollEvent event2 = o.takeNext();
    assertNotNull(event2);
    assertEquals(-50, event2.dy());

    // Back at position 0. Trying to scroll earlier shouldn't fire any events
    instrumentation.runOnMainSync(() -> view.scrollBy(0, -50));
    o.assertNoMoreEvents();

    o.dispose();

    instrumentation.runOnMainSync(() -> view.scrollBy(0, 50));
    o.assertNoMoreEvents();
  }

  @Test public void scrollEventsHorizontal() {
    instrumentation.runOnMainSync(() -> {
      view.setAdapter(new Adapter());
      ((LinearLayoutManager) view.getLayoutManager()).setOrientation(LinearLayoutManager.HORIZONTAL);
    });

    instrumentation.waitForIdleSync();
    RecordingObserver<RecyclerViewScrollEvent> o = new RecordingObserver<>();
    RxRecyclerView.scrollEvents(view)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(() -> view.scrollBy(50, 0));
    RecyclerViewScrollEvent event3 = o.takeNext();
    assertNotNull(event3);
    assertEquals(50, event3.dx());

    instrumentation.runOnMainSync(() -> view.scrollBy(0, 0));
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(() -> view.scrollBy(-50, 0));
    RecyclerViewScrollEvent event4 = o.takeNext();
    assertNotNull(event4);
    assertEquals(-50, event4.dx());

    // Back at position 0. Trying to scroll earlier shouldn't fire any events
    instrumentation.runOnMainSync(() -> view.scrollBy(-50, 0));
    o.assertNoMoreEvents();

    o.dispose();

    instrumentation.runOnMainSync(() -> view.scrollBy(50, 0));
    o.assertNoMoreEvents();
  }

  @Test public void flingEventsVertical() {
    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.setAdapter(new Adapter());
      }
    });

    RecordingObserver<RecyclerViewFlingEvent> o = new RecordingObserver<>();
    RxRecyclerView.flingEvents(view)
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(o);
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.fling(0, 1000);
      }
    });
    RecyclerViewFlingEvent event1 = o.takeNext();
    assertNotNull(event1);
    assertEquals(1000, event1.velocityY());

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.fling(0, 0);
      }
    });
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.fling(0, -1000);
      }
    });
    RecyclerViewFlingEvent event2 = o.takeNext();
    assertNotNull(event2);
    assertEquals(-1000, event2.velocityY());

    o.dispose();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.fling(0, 1000);
      }
    });
    o.assertNoMoreEvents();
  }

  @Test public void flingEventsHorizontal() {
    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.setAdapter(new Adapter());
        ((LinearLayoutManager) view.getLayoutManager()).setOrientation(LinearLayoutManager.HORIZONTAL);
      }
    });

    instrumentation.waitForIdleSync();
    RecordingObserver<RecyclerViewFlingEvent> o = new RecordingObserver<>();
    RxRecyclerView.flingEvents(view)
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(o);
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.fling(1000, 0);
      }
    });
    RecyclerViewFlingEvent event1 = o.takeNext();
    assertNotNull(event1);
    assertEquals(1000, event1.velocityX());

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.fling(0, 0);
      }
    });
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.fling(-1000, 0);
      }
    });
    RecyclerViewFlingEvent event2 = o.takeNext();
    assertNotNull(event2);
    assertEquals(-1000, event2.velocityX());

    o.dispose();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.fling(1000, 0);
      }
    });
    o.assertNoMoreEvents();
  }

  private class SimpleAdapter extends RecyclerView.Adapter {
    private final View child;

    SimpleAdapter(View child) {
      this.child = child;
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new RecyclerView.ViewHolder(child) {
      };
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    @Override public int getItemCount() {
      return 1;
    }
  }

  private static class Adapter extends RecyclerView.Adapter<ViewHolder> {
    public Adapter() {
      setHasStableIds(true);
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
      TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
      return new ViewHolder(v);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
      holder.textView.setText(String.valueOf(position));
    }

    @Override public int getItemCount() {
      return 100;
    }

    @Override public long getItemId(int position) {
      return position;
    }
  }

  private static class ViewHolder extends RecyclerView.ViewHolder {

    TextView textView;

    ViewHolder(TextView itemView) {
      super(itemView);
      this.textView = itemView;
    }
  }
}
