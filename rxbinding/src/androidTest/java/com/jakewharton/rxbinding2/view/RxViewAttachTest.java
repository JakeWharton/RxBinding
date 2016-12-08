package com.jakewharton.rxbinding2.view;

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

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.google.common.truth.Truth.assertThat;
import static com.jakewharton.rxbinding2.view.ViewAttachEvent.Kind.ATTACH;
import static com.jakewharton.rxbinding2.view.ViewAttachEvent.Kind.DETACH;

@RunWith(AndroidJUnit4.class)
public final class RxViewAttachTest {
  @Rule public final ActivityTestRule<RxViewAttachTestActivity> activityRule =
      new ActivityTestRule<>(RxViewAttachTestActivity.class);

  private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
  private FrameLayout parent;
  private View child;

  @Before public void setUp() {
    RxViewAttachTestActivity activity = activityRule.getActivity();
    parent = activity.parent;
    child = activity.child;
  }

  @Test public void attaches() {
    RecordingObserver<Object> o = new RecordingObserver<>();
    RxView.attaches(child)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents(); // No initial value.

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        parent.addView(child);
      }
    });
    assertThat(o.takeNext()).isNotNull();
    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        parent.removeView(child);
      }
    });
    o.assertNoMoreEvents();

    o.dispose();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        parent.addView(child);
        parent.removeView(child);
      }
    });
    o.assertNoMoreEvents();
  }

  @Test public void attachEvents() {
    RecordingObserver<ViewAttachEvent> o = new RecordingObserver<>();
    RxView.attachEvents(child)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents(); // No initial value.

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        parent.addView(child);
      }
    });
    assertThat(o.takeNext().kind()).isEqualTo(ATTACH);
    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        parent.removeView(child);
      }
    });
    assertThat(o.takeNext().kind()).isEqualTo(DETACH);

    o.dispose();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        parent.addView(child);
        parent.removeView(child);
      }
    });
    o.assertNoMoreEvents();
  }

  @Test public void detaches() {
    RecordingObserver<Object> o = new RecordingObserver<>();
    RxView.detaches(child)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents(); // No initial value.

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        parent.addView(child);
      }
    });
    o.assertNoMoreEvents();
    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        parent.removeView(child);
      }
    });
    assertThat(o.takeNext()).isNotNull();

    o.dispose();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        parent.addView(child);
        parent.removeView(child);
      }
    });
    o.assertNoMoreEvents();
  }
}
