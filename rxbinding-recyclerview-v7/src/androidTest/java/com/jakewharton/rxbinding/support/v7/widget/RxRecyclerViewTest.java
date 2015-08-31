package com.jakewharton.rxbinding.support.v7.widget;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jakewharton.rxbinding.RecordingObserver;
import com.jakewharton.rxbinding.ViewDirtyIdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;
import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public final class RxRecyclerViewTest {
  @Rule public final ActivityTestRule<RxRecyclerViewTestActivity> activityRule =
      new ActivityTestRule<>(RxRecyclerViewTestActivity.class);

  private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

  private RecyclerView view;
  private ViewInteraction interaction;
  private ViewDirtyIdlingResource viewDirtyIdler;

  @Before public void setUp() {
    RxRecyclerViewTestActivity activity = activityRule.getActivity();
    view = activity.recyclerView;
    interaction = Espresso.onView(ViewMatchers.withId(android.R.id.primary));
    viewDirtyIdler = new ViewDirtyIdlingResource(activity);
    Espresso.registerIdlingResources(viewDirtyIdler);
  }

  @After public void tearDown() {
    Espresso.unregisterIdlingResources(viewDirtyIdler);
  }

  @Test public void scrollEventsVertical() {
    RecordingObserver<RecyclerViewScrollEvent> o = new RecordingObserver<>();
    Subscription subscription = RxRecyclerView.scrollEvents(view)
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

    subscription.unsubscribe();

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
        ((LinearLayoutManager) view.getLayoutManager()).setOrientation(LinearLayoutManager.HORIZONTAL);
      }
    });

    instrumentation.waitForIdleSync();
    RecordingObserver<RecyclerViewScrollEvent> o = new RecordingObserver<>();
    Subscription subscription = RxRecyclerView.scrollEvents(view)
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

    subscription.unsubscribe();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.scrollBy(50, 0);
      }
    });
    o.assertNoMoreEvents();
  }

  @Test public void scrollStateChangeEventsVertical() {
    RecordingObserver<RecyclerViewScrollStateChangeEvent> o = new RecordingObserver<>();
    Subscription subscription = RxRecyclerView.scrollStateChangeEvents(view)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents();

    instrumentation.waitForIdleSync();
    interaction.perform(swipeUp());
    instrumentation.waitForIdleSync();
    assertThat(o.takeNext().newState()).isEqualTo(SCROLL_STATE_DRAGGING);
    assertThat(o.takeNext().newState()).isEqualTo(SCROLL_STATE_SETTLING);
    assertThat(o.takeNext().newState()).isEqualTo(SCROLL_STATE_IDLE);

    subscription.unsubscribe();

    instrumentation.waitForIdleSync();
    interaction.perform(swipeDown());
    instrumentation.waitForIdleSync();
    o.assertNoMoreEvents();
  }

  @Test public void scrollStateChangeEventsHorizontal() {
    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        ((LinearLayoutManager) view.getLayoutManager()).setOrientation(LinearLayoutManager.HORIZONTAL);
      }
    });

    instrumentation.waitForIdleSync();
    RecordingObserver<RecyclerViewScrollStateChangeEvent> o = new RecordingObserver<>();
    Subscription subscription = RxRecyclerView.scrollStateChangeEvents(view)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents();

    interaction.perform(swipeLeft());
    instrumentation.waitForIdleSync();
    assertThat(o.takeNext().newState()).isEqualTo(SCROLL_STATE_DRAGGING);
    assertThat(o.takeNext().newState()).isEqualTo(SCROLL_STATE_SETTLING);
    assertThat(o.takeNext().newState()).isEqualTo(SCROLL_STATE_IDLE);

    subscription.unsubscribe();

    instrumentation.waitForIdleSync();
    interaction.perform(swipeRight());
    instrumentation.waitForIdleSync();
    o.assertNoMoreEvents();
  }

  @Test public void itemClicks() {
    RecordingObserver<Integer> o = new RecordingObserver<>();
    Subscription subscription = RxRecyclerView.itemClicks(view)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.findViewHolderForLayoutPosition(1).itemView.performClick();
      }
    });
    assertThat(o.takeNext()).isEqualTo(1);

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.smoothScrollToPosition(50);
        view.findViewHolderForLayoutPosition(5).itemView.performClick();
      }
    });
    assertThat(o.takeNext()).isEqualTo(5);

    subscription.unsubscribe();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.smoothScrollToPosition(50);
        view.findViewHolderForLayoutPosition(5).itemView.performClick();
      }
    });
    o.assertNoMoreEvents();
  }
}
