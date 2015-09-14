package com.jakewharton.rxbinding.support.v7.widget;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.jakewharton.rxbinding.RecordingObserver;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class) public final class RxRecyclerViewChildAttachStateTest {
  @Rule public final ActivityTestRule<RxRecyclerViewChildAttachStateTestActivity> activityRule =
      new ActivityTestRule<>(RxRecyclerViewChildAttachStateTestActivity.class);

  private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
  private RecyclerView view;
  private View child;

  @Before public void setUp() {
    RxRecyclerViewChildAttachStateTestActivity activity = activityRule.getActivity();
    view = activity.recyclerView;
    child = new View(activityRule.getActivity());
  }

  @Test public void childAttachEvents() {
    RecordingObserver<RecyclerViewChildAttachStateChangeEvent> o = new RecordingObserver<>();
    Subscription subscription = RxRecyclerView.childAttachStateChangeEvents(view)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents();

    final SimpleAdapter adapter = new SimpleAdapter(child);

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.setAdapter(adapter);
      }
    });
    assertThat(o.takeNext()).isEqualTo(RecyclerViewChildAttachEvent.create(view, child));

    subscription.unsubscribe();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.setAdapter(adapter);
      }
    });

    o.assertNoMoreEvents();
  }

  @Test public void childDetachEvents() {
    final SimpleAdapter adapter = new SimpleAdapter(child);

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.setAdapter(adapter);
      }
    });

    RecordingObserver<RecyclerViewChildAttachStateChangeEvent> o = new RecordingObserver<>();
    Subscription subscription = RxRecyclerView.childAttachStateChangeEvents(view)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.setAdapter(null);
      }
    });
    assertThat(o.takeNext()).isEqualTo(RecyclerViewChildDetachEvent.create(view, child));

    subscription.unsubscribe();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.setAdapter(adapter);
      }
    });

    o.assertNoMoreEvents();
  }

  public class SimpleAdapter extends RecyclerView.Adapter {
    private final View child;

    public SimpleAdapter(View child) {
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
}
