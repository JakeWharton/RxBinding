package com.jakewharton.rxbinding.widget;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.SeekBar;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import rx.Subscription;
import com.jakewharton.rxbinding.RecordingObserver;
import rx.android.schedulers.HandlerSchedulers;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;
import static com.google.common.truth.Truth.assertThat;
import static com.jakewharton.rxbinding.MotionEventUtil.motionEventAtPosition;

@RunWith(AndroidJUnit4.class)
public final class RxSeekBarTest {
  @Rule public final ActivityTestRule<RxSeekBarTestActivity> activityRule =
      new ActivityTestRule<>(RxSeekBarTestActivity.class);

  private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

  private SeekBar seekBar;

  @Before public void setUp() {
    seekBar = activityRule.getActivity().seekBar;
  }

  @Test public void changes() {
    RecordingObserver<Integer> o = new RecordingObserver<>();
    Subscription subscription = RxSeekBar.changes(seekBar) //
        .subscribeOn(HandlerSchedulers.mainThread()) //
        .subscribe(o);
    o.assertNoMoreEvents();

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_DOWN, 0));
    instrumentation.waitForIdleSync();
    o.assertNoMoreEvents();

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_MOVE, 100));
    instrumentation.waitForIdleSync();
    assertThat(o.takeNext()).isEqualTo(100);

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_MOVE, 0));
    instrumentation.waitForIdleSync();
    assertThat(o.takeNext()).isEqualTo(0);

    subscription.unsubscribe();

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_MOVE, 100));
    instrumentation.waitForIdleSync();
    o.assertNoMoreEvents();
  }

  @Test public void changeEvents() {
    RecordingObserver<SeekBarChangeEvent> o = new RecordingObserver<>();
    Subscription subscription = RxSeekBar.changeEvents(seekBar) //
        .subscribeOn(HandlerSchedulers.mainThread()) //
        .subscribe(o);
    o.assertNoMoreEvents();

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_DOWN, 0));
    instrumentation.waitForIdleSync();
    assertThat(o.takeNext()).isEqualTo(SeekBarStartChangeEvent.create(seekBar));

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_MOVE, 100));
    instrumentation.waitForIdleSync();
    assertThat(o.takeNext()).isEqualTo(SeekBarProgressChangeEvent.create(seekBar, 100, true));

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_UP, 100));
    instrumentation.waitForIdleSync();
    assertThat(o.takeNext()).isEqualTo(SeekBarStopChangeEvent.create(seekBar));

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        seekBar.setProgress(0);
      }
    });
    instrumentation.waitForIdleSync();
    assertThat(o.takeNext()).isEqualTo(SeekBarProgressChangeEvent.create(seekBar, 0, false));

    subscription.unsubscribe();

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_DOWN, 0));
    instrumentation.waitForIdleSync();
    o.assertNoMoreEvents();
  }
}
