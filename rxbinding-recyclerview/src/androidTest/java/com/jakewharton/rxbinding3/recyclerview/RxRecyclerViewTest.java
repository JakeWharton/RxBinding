package com.jakewharton.rxbinding3.recyclerview;

import android.app.Instrumentation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.jakewharton.rxbinding.ViewDirtyIdlingResource;
import com.jakewharton.rxbinding2.RecordingObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
    assertEquals(new RecyclerViewChildAttachEvent(view, child), o.takeNext());

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
    assertEquals(new RecyclerViewChildDetachEvent(view, child), o.takeNext());

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
    assertEquals(50, event1.getDy());

    instrumentation.runOnMainSync(() -> view.scrollBy(0, 0));
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(() -> view.scrollBy(0, -50));
    RecyclerViewScrollEvent event2 = o.takeNext();
    assertNotNull(event2);
    assertEquals(-50, event2.getDy());

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
    assertEquals(50, event3.getDx());

    instrumentation.runOnMainSync(() -> view.scrollBy(0, 0));
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(() -> view.scrollBy(-50, 0));
    RecyclerViewScrollEvent event4 = o.takeNext();
    assertNotNull(event4);
    assertEquals(-50, event4.getDx());

    // Back at position 0. Trying to scroll earlier shouldn't fire any events
    instrumentation.runOnMainSync(() -> view.scrollBy(-50, 0));
    o.assertNoMoreEvents();

    o.dispose();

    instrumentation.runOnMainSync(() -> view.scrollBy(50, 0));
    o.assertNoMoreEvents();
  }

  @Test public void flingEventsVertical() {
    instrumentation.runOnMainSync(() -> view.setAdapter(new Adapter()));

    RecordingObserver<RecyclerViewFlingEvent> o = new RecordingObserver<>();
    RxRecyclerView.flingEvents(view)
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(o);
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(() -> view.fling(0, 1000));
    RecyclerViewFlingEvent event1 = o.takeNext();
    assertNotNull(event1);
    assertEquals(1000, event1.getVelocityY());

    instrumentation.runOnMainSync(() -> view.fling(0, 0));
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(() -> view.fling(0, -1000));
    RecyclerViewFlingEvent event2 = o.takeNext();
    assertNotNull(event2);
    assertEquals(-1000, event2.getVelocityY());

    o.dispose();

    instrumentation.runOnMainSync(() -> view.fling(0, 1000));
    o.assertNoMoreEvents();
  }

  @Test public void flingEventsHorizontal() {
    instrumentation.runOnMainSync(() -> {
      view.setAdapter(new Adapter());
      ((LinearLayoutManager) view.getLayoutManager()).setOrientation(LinearLayoutManager.HORIZONTAL);
    });
    instrumentation.waitForIdleSync();

    RecordingObserver<RecyclerViewFlingEvent> o = new RecordingObserver<>();
    RxRecyclerView.flingEvents(view)
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(o);
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(() -> view.fling(1000, 0));
    RecyclerViewFlingEvent event1 = o.takeNext();
    assertNotNull(event1);
    assertEquals(1000, event1.getVelocityX());

    instrumentation.runOnMainSync(() -> view.fling(0, 0));
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(() -> view.fling(-1000, 0));
    RecyclerViewFlingEvent event2 = o.takeNext();
    assertNotNull(event2);
    assertEquals(-1000, event2.getVelocityX());

    o.dispose();

    instrumentation.runOnMainSync(() -> view.fling(1000, 0));
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
