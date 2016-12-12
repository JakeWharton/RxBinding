package com.jakewharton.rxbinding2.widget;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.SeekBar;

import com.jakewharton.rxbinding2.RecordingObserver;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.android.schedulers.AndroidSchedulers;

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
    RxSeekBar.changes(seekBar) //
        .subscribeOn(AndroidSchedulers.mainThread()) //
        .subscribe(o);
    assertThat(o.takeNext()).isEqualTo(0);

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_DOWN, 0, 50));
    instrumentation.waitForIdleSync();
    o.assertNoMoreEvents();

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_MOVE, 100, 50));
    instrumentation.waitForIdleSync();
    assertThat(o.takeNext()).isEqualTo(100);

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_MOVE, 0, 50));
    instrumentation.waitForIdleSync();
    assertThat(o.takeNext()).isEqualTo(0);

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        seekBar.setProgress(85);
      }
    });
    instrumentation.waitForIdleSync();
    assertThat(o.takeNext()).isEqualTo(85);

    o.dispose();

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_MOVE, 100, 50));
    instrumentation.waitForIdleSync();
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        seekBar.setProgress(85);
      }
    });
    instrumentation.waitForIdleSync();
    o.assertNoMoreEvents();
  }

  @Test public void systemChanges() {
    RecordingObserver<Integer> o = new RecordingObserver<>();
    RxSeekBar.systemChanges(seekBar) //
            .subscribeOn(AndroidSchedulers.mainThread()) //
            .subscribe(o);
    assertThat(o.takeNext()).isEqualTo(0);

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_MOVE, 100, 50));
    instrumentation.waitForIdleSync();
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        seekBar.setProgress(85);
      }
    });
    instrumentation.waitForIdleSync();
    assertThat(o.takeNext()).isEqualTo(85);

    o.dispose();

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_MOVE, 100, 50));
    instrumentation.waitForIdleSync();
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        seekBar.setProgress(85);
      }
    });
    instrumentation.waitForIdleSync();
    o.assertNoMoreEvents();
  }

  @Test public void userChanges() {
    RecordingObserver<Integer> o = new RecordingObserver<>();
    RxSeekBar.userChanges(seekBar) //
            .subscribeOn(AndroidSchedulers.mainThread()) //
            .subscribe(o);
    assertThat(o.takeNext()).isEqualTo(0);

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_DOWN, 0, 50));
    instrumentation.waitForIdleSync();
    o.assertNoMoreEvents();

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_MOVE, 100, 50));
    instrumentation.waitForIdleSync();
    assertThat(o.takeNext()).isEqualTo(100);

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_MOVE, 0, 50));
    instrumentation.waitForIdleSync();
    assertThat(o.takeNext()).isEqualTo(0);

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        seekBar.setProgress(85);
      }
    });
    instrumentation.waitForIdleSync();
    o.assertNoMoreEvents();

    o.dispose();

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_MOVE, 100, 50));
    instrumentation.waitForIdleSync();
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        seekBar.setProgress(85);
      }
    });
    instrumentation.waitForIdleSync();
    o.assertNoMoreEvents();
  }

  @Test public void changeEvents() {
    RecordingObserver<SeekBarChangeEvent> o = new RecordingObserver<>();
    RxSeekBar.changeEvents(seekBar) //
        .subscribeOn(AndroidSchedulers.mainThread()) //
        .subscribe(o);
    assertThat(o.takeNext()).isEqualTo(SeekBarProgressChangeEvent.create(seekBar, 0, false));

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_DOWN, 0, 50));
    instrumentation.waitForIdleSync();
    assertThat(o.takeNext()).isEqualTo(SeekBarStartChangeEvent.create(seekBar));

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_MOVE, 100, 50));
    instrumentation.waitForIdleSync();
    assertThat(o.takeNext()).isEqualTo(SeekBarProgressChangeEvent.create(seekBar, 100, true));

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_UP, 100, 50));
    instrumentation.waitForIdleSync();
    assertThat(o.takeNext()).isEqualTo(SeekBarStopChangeEvent.create(seekBar));

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        seekBar.setProgress(0);
      }
    });
    instrumentation.waitForIdleSync();
    assertThat(o.takeNext()).isEqualTo(SeekBarProgressChangeEvent.create(seekBar, 0, false));

    o.dispose();

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_DOWN, 0, 50));
    instrumentation.waitForIdleSync();
    o.assertNoMoreEvents();
  }
}
