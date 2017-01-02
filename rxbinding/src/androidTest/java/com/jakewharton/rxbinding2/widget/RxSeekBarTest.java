package com.jakewharton.rxbinding2.widget;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.SeekBar;
import com.jakewharton.rxbinding2.RecordingObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;
import static com.jakewharton.rxbinding.MotionEventUtil.motionEventAtPosition;
import static org.junit.Assert.assertEquals;

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
    assertEquals(0, o.takeNext().intValue());

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_DOWN, 0, 50));
    instrumentation.waitForIdleSync();
    o.assertNoMoreEvents();

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_MOVE, 100, 50));
    instrumentation.waitForIdleSync();
    assertEquals(100, o.takeNext().intValue());

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_MOVE, 0, 50));
    instrumentation.waitForIdleSync();
    assertEquals(0, o.takeNext().intValue());

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        seekBar.setProgress(85);
      }
    });
    instrumentation.waitForIdleSync();
    assertEquals(85, o.takeNext().intValue());

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
    assertEquals(0, o.takeNext().intValue());

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_MOVE, 100, 50));
    instrumentation.waitForIdleSync();
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        seekBar.setProgress(85);
      }
    });
    instrumentation.waitForIdleSync();
    assertEquals(85, o.takeNext().intValue());

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
    assertEquals(0, o.takeNext().intValue());

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_DOWN, 0, 50));
    instrumentation.waitForIdleSync();
    o.assertNoMoreEvents();

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_MOVE, 100, 50));
    instrumentation.waitForIdleSync();
    assertEquals(100, o.takeNext().intValue());

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_MOVE, 0, 50));
    instrumentation.waitForIdleSync();
    assertEquals(0, o.takeNext().intValue());

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
    assertEquals(SeekBarProgressChangeEvent.create(seekBar, 0, false), o.takeNext());

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_DOWN, 0, 50));
    instrumentation.waitForIdleSync();
    assertEquals(SeekBarStartChangeEvent.create(seekBar), o.takeNext());

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_MOVE, 100, 50));
    instrumentation.waitForIdleSync();
    assertEquals(SeekBarProgressChangeEvent.create(seekBar, 100, true), o.takeNext());

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_UP, 100, 50));
    instrumentation.waitForIdleSync();
    assertEquals(SeekBarStopChangeEvent.create(seekBar), o.takeNext());

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        seekBar.setProgress(0);
      }
    });
    instrumentation.waitForIdleSync();
    assertEquals(SeekBarProgressChangeEvent.create(seekBar, 0, false), o.takeNext());

    o.dispose();

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_DOWN, 0, 50));
    instrumentation.waitForIdleSync();
    o.assertNoMoreEvents();
  }
}
