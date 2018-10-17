package com.jakewharton.rxbinding3.widget;

import android.app.Instrumentation;
import android.widget.SeekBar;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.jakewharton.rxbinding3.RecordingObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;
import static com.jakewharton.rxbinding3.MotionEventUtil.motionEventAtPosition;
import static org.junit.Assert.assertEquals;

public final class RxSeekBarTest {
  @Rule public final ActivityTestRule<RxSeekBarTestActivity> activityRule =
      new ActivityTestRule<>(RxSeekBarTestActivity.class);

  private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

  SeekBar seekBar;

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

    instrumentation.runOnMainSync(() -> seekBar.setProgress(85));
    instrumentation.waitForIdleSync();
    assertEquals(85, o.takeNext().intValue());

    o.dispose();

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_MOVE, 100, 50));
    instrumentation.waitForIdleSync();
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(() -> seekBar.setProgress(85));
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

    instrumentation.runOnMainSync(() -> seekBar.setProgress(85));
    instrumentation.waitForIdleSync();
    assertEquals(85, o.takeNext().intValue());

    o.dispose();

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_MOVE, 100, 50));
    instrumentation.waitForIdleSync();
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(() -> seekBar.setProgress(85));
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

    instrumentation.runOnMainSync(() -> seekBar.setProgress(85));
    instrumentation.waitForIdleSync();
    o.assertNoMoreEvents();

    o.dispose();

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_MOVE, 100, 50));
    instrumentation.waitForIdleSync();
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(() -> seekBar.setProgress(85));
    instrumentation.waitForIdleSync();
    o.assertNoMoreEvents();
  }

  @Test public void changeEvents() {
    RecordingObserver<SeekBarChangeEvent> o = new RecordingObserver<>();
    RxSeekBar.changeEvents(seekBar) //
        .subscribeOn(AndroidSchedulers.mainThread()) //
        .subscribe(o);
    assertEquals(new SeekBarProgressChangeEvent(seekBar, 0, false), o.takeNext());

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_DOWN, 0, 50));
    instrumentation.waitForIdleSync();
    assertEquals(new SeekBarStartChangeEvent(seekBar), o.takeNext());

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_MOVE, 100, 50));
    instrumentation.waitForIdleSync();
    assertEquals(new SeekBarProgressChangeEvent(seekBar, 100, true), o.takeNext());

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_UP, 100, 50));
    instrumentation.waitForIdleSync();
    assertEquals(new SeekBarStopChangeEvent(seekBar), o.takeNext());

    instrumentation.runOnMainSync(() -> seekBar.setProgress(0));
    instrumentation.waitForIdleSync();
    assertEquals(new SeekBarProgressChangeEvent(seekBar, 0, false), o.takeNext());

    o.dispose();

    instrumentation.sendPointerSync(motionEventAtPosition(seekBar, ACTION_DOWN, 0, 50));
    instrumentation.waitForIdleSync();
    o.assertNoMoreEvents();
  }
}
