package com.jakewharton.rxbinding2.support.v7.widget;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jakewharton.rxbinding2.RecordingObserver;
import com.jakewharton.rxbinding.ViewDirtyIdlingResource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public final class RxRecyclerViewTest {
  @Rule public final ActivityTestRule<RxRecyclerViewTestActivity> activityRule =
      new ActivityTestRule<>(RxRecyclerViewTestActivity.class);

  private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

  private RecyclerView view;
  private ViewInteraction interaction;
  private ViewDirtyIdlingResource viewDirtyIdler;
  private View child;

  @Before public void setUp() {
    RxRecyclerViewTestActivity activity = activityRule.getActivity();
    view = activity.recyclerView;
    child = new View(activityRule.getActivity());
    interaction = Espresso.onView(ViewMatchers.withId(android.R.id.primary));
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

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.setAdapter(adapter);
      }
    });
    assertThat(o.takeNext()).isEqualTo(RecyclerViewChildAttachEvent.create(view, child));

    o.dispose();

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
    RxRecyclerView.childAttachStateChangeEvents(view)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.setAdapter(null);
      }
    });
    assertThat(o.takeNext()).isEqualTo(RecyclerViewChildDetachEvent.create(view, child));

    o.dispose();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.setAdapter(adapter);
      }
    });

    o.assertNoMoreEvents();
  }

  @Test public void scrollEventsVertical() {
    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.setAdapter(new Adapter());
      }
    });

    RecordingObserver<RecyclerViewScrollEvent> o = new RecordingObserver<>();
    RxRecyclerView.scrollEvents(view)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.scrollBy(0, 50);
      }
    });
    RecyclerViewScrollEvent event1 = o.takeNext();
    assertThat(event1).isNotNull();
    assertThat(event1.dy()).isEqualTo(50);

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.scrollBy(0, 0);
      }
    });
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.scrollBy(0, -50);
      }
    });
    RecyclerViewScrollEvent event2 = o.takeNext();
    assertThat(event2).isNotNull();
    assertThat(event2.dy()).isEqualTo(-50);

    // Back at position 0. Trying to scroll earlier shouldn't fire any events
    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.scrollBy(0, -50);
      }
    });
    o.assertNoMoreEvents();

    o.dispose();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.scrollBy(0, 50);
      }
    });
    o.assertNoMoreEvents();
  }

  @Test public void scrollEventsHorizontal() {
    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.setAdapter(new Adapter());
        ((LinearLayoutManager) view.getLayoutManager()).setOrientation(LinearLayoutManager.HORIZONTAL);
      }
    });

    instrumentation.waitForIdleSync();
    RecordingObserver<RecyclerViewScrollEvent> o = new RecordingObserver<>();
    RxRecyclerView.scrollEvents(view)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.scrollBy(50, 0);
      }
    });
    RecyclerViewScrollEvent event3 = o.takeNext();
    assertThat(event3).isNotNull();
    assertThat(event3.dx()).isEqualTo(50);

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.scrollBy(0, 0);
      }
    });
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.scrollBy(-50, 0);
      }
    });
    RecyclerViewScrollEvent event4 = o.takeNext();
    assertThat(event4).isNotNull();
    assertThat(event4.dx()).isEqualTo(-50);

    // Back at position 0. Trying to scroll earlier shouldn't fire any events
    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.scrollBy(-50, 0);
      }
    });
    o.assertNoMoreEvents();

    o.dispose();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.scrollBy(50, 0);
      }
    });
    o.assertNoMoreEvents();
  }

  private class SimpleAdapter extends RecyclerView.Adapter {
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

    public ViewHolder(TextView itemView) {
      super(itemView);
      this.textView = itemView;
    }
  }
}
