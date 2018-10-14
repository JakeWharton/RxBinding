package com.jakewharton.rxbinding2.widget;

import android.app.Instrumentation;
import android.widget.RatingBar;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.jakewharton.rxbinding2.RecordingObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;
import static com.jakewharton.rxbinding.MotionEventUtil.motionEventAtPosition;
import static org.junit.Assert.assertEquals;

public final class RxRatingBarTest {
  @Rule public final ActivityTestRule<RxRatingBarTestActivity> activityRule =
      new ActivityTestRule<>(RxRatingBarTestActivity.class);

  private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

  RatingBar view;

  @Before public void setUp() {
    view = activityRule.getActivity().ratingBar;
  }

  @Test public void ratingChanges() {
    RecordingObserver<Float> o = new RecordingObserver<>();
    RxRatingBar.ratingChanges(view)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    assertEquals(0f, o.takeNext(), 0f);

    instrumentation.runOnMainSync(() -> view.setRating(1f));
    assertEquals(1f, o.takeNext(), 0f);

    instrumentation.runOnMainSync(() -> view.setRating(2f));
    assertEquals(2f, o.takeNext(), 0f);

    o.dispose();

    instrumentation.runOnMainSync(() -> view.setRating(1f));
    o.assertNoMoreEvents();
  }

  @Test public void ratingChangeEvents() {
    RecordingObserver<RatingBarChangeEvent> o = new RecordingObserver<>();
    RxRatingBar.ratingChangeEvents(view) //
        .subscribeOn(AndroidSchedulers.mainThread()) //
        .subscribe(o);
    assertEquals(new RatingBarChangeEvent(view, 0f, false), o.takeNext());

    instrumentation.runOnMainSync(() -> view.setRating(5f));
    assertEquals(new RatingBarChangeEvent(view, 5f, false), o.takeNext());

    instrumentation.sendPointerSync(motionEventAtPosition(view, ACTION_DOWN, 0, 50));
    instrumentation.sendPointerSync(motionEventAtPosition(view, ACTION_UP, 0, 50));
    instrumentation.waitForIdleSync();
    assertEquals(new RatingBarChangeEvent(view, 1f, true), o.takeNext());

    o.dispose();

    instrumentation.runOnMainSync(() -> view.setRating(1f));
    o.assertNoMoreEvents();
  }
}
