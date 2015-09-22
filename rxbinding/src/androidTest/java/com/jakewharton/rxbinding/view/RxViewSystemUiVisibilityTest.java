package com.jakewharton.rxbinding.view;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.FrameLayout;
import com.jakewharton.rxbinding.RecordingObserver;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public final class RxViewSystemUiVisibilityTest {
  @Rule public final ActivityTestRule<RxViewSystemUiVisibilityTestActivity> activityRule =
      new ActivityTestRule<>(RxViewSystemUiVisibilityTestActivity.class);

  private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
  private FrameLayout root;

  @Before public void setUp() {
    RxViewSystemUiVisibilityTestActivity activity = activityRule.getActivity();
    root = activity.root;
    root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
  }

  @Test public void systemUiVisibilityChanges() {
    RecordingObserver<Integer> o = new RecordingObserver<>();
    Subscription subscription = RxView.systemUiVisibilityChanges(root)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
      }
    });
    assertThat(o.takeNext()).isEqualTo(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
      }
    });
    assertThat(o.takeNext()).isEqualTo(View.SYSTEM_UI_FLAG_VISIBLE);

    subscription.unsubscribe();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
      }
    });
    o.assertNoMoreEvents();
  }

  @Test public void systemUiVisibilityChangeEvents() {
    RecordingObserver<ViewSystemUiVisibilityChangeEvent> o = new RecordingObserver<>();
    Subscription subscription = RxView.systemUiVisibilityChangeEvents(root)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
      }
    });
    ViewSystemUiVisibilityChangeEvent event1 = o.takeNext();
    assertThat(event1.view()).isSameAs(root);
    assertThat(event1.visibility()).isEqualTo(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
      }
    });
    ViewSystemUiVisibilityChangeEvent event2 = o.takeNext();
    assertThat(event2.view()).isSameAs(root);
    assertThat(event2.visibility()).isEqualTo(View.SYSTEM_UI_FLAG_VISIBLE);

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
      }
    });
    o.assertNoMoreEvents();

    subscription.unsubscribe();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
      }
    });
    o.assertNoMoreEvents();
  }
}
