package rx.android.widget;

import android.app.Instrumentation;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.MotionEvent;
import android.widget.SeekBar;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import rx.Subscription;
import rx.android.RecordingObserver;
import rx.android.schedulers.HandlerSchedulers;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;
import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public final class RxSeekBarTest {
  @Rule public final ActivityTestRule<RxSeekBarTestActivity> activityRule =
      new ActivityTestRule<>(RxSeekBarTestActivity.class);

  private Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

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

    instrumentation.sendPointerSync(motionEventAtPosition(ACTION_DOWN, 0));
    instrumentation.waitForIdleSync();
    o.assertNoMoreEvents();

    instrumentation.sendPointerSync(motionEventAtPosition(ACTION_MOVE, 100));
    instrumentation.waitForIdleSync();
    assertThat(o.takeNext()).isEqualTo(100);

    instrumentation.sendPointerSync(motionEventAtPosition(ACTION_MOVE, 0));
    instrumentation.waitForIdleSync();
    assertThat(o.takeNext()).isEqualTo(0);

    subscription.unsubscribe();

    instrumentation.sendPointerSync(motionEventAtPosition(ACTION_MOVE, 100));
    instrumentation.waitForIdleSync();
    o.assertNoMoreEvents();
  }

  @Test public void changeEvents() {
    RecordingObserver<SeekBarChangeEvent> o = new RecordingObserver<>();
    Subscription subscription = RxSeekBar.changeEvents(seekBar) //
        .subscribeOn(HandlerSchedulers.mainThread()) //
        .subscribe(o);
    o.assertNoMoreEvents();

    instrumentation.sendPointerSync(motionEventAtPosition(ACTION_DOWN, 0));
    instrumentation.waitForIdleSync();
    assertThat(o.takeNext()).isEqualTo(SeekBarStartChangeEvent.create(seekBar));

    instrumentation.sendPointerSync(motionEventAtPosition(ACTION_MOVE, 100));
    instrumentation.waitForIdleSync();
    assertThat(o.takeNext()).isEqualTo(SeekBarProgressChangeEvent.create(seekBar, 100, true));

    instrumentation.sendPointerSync(motionEventAtPosition(ACTION_UP, 100));
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

    instrumentation.sendPointerSync(motionEventAtPosition(ACTION_DOWN, 0));
    instrumentation.waitForIdleSync();
    o.assertNoMoreEvents();
  }

  private MotionEvent motionEventAtPosition(int action, int position) {
    // NOTE: This method is not perfect. If you send touch events in a granular nature, you'll
    // see varying results of accuracy depending on the size of the jump.

    int paddingLeft = seekBar.getPaddingLeft();
    int paddingRight = seekBar.getPaddingRight();
    int paddingTop = seekBar.getPaddingTop();
    int paddingBottom = seekBar.getPaddingBottom();

    int width = seekBar.getWidth();
    int height = seekBar.getHeight();

    int topLeft[] = new int[2];
    seekBar.getLocationInWindow(topLeft);
    int x1 = topLeft[0] + paddingLeft;
    int y1 = topLeft[1] + paddingTop;
    int x2 = x1 + width - paddingLeft - paddingRight;
    int y2 = y1 + height - paddingTop - paddingBottom;

    float x = x1 + ((x2 - x1) * position / 100f);
    float y = y1 + ((y2 - y1) / 2f);

    long time = SystemClock.uptimeMillis();
    return MotionEvent.obtain(time, time, action, x, y, 0);
  }
}
