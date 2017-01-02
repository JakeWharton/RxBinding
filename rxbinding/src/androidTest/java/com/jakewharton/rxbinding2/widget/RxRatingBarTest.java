package com.jakewharton.rxbinding2.widget;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.RatingBar;
import com.jakewharton.rxbinding2.RecordingObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;
import static com.jakewharton.rxbinding.MotionEventUtil.motionEventAtPosition;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class) public final class RxRatingBarTest {
  @Rule public final ActivityTestRule<RxRatingBarTestActivity> activityRule =
      new ActivityTestRule<>(RxRatingBarTestActivity.class);

  private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

  private RatingBar view;

  @Before public void setUp() {
    view = activityRule.getActivity().ratingBar;
  }

  @Test public void ratingChanges() {
    RecordingObserver<Float> o = new RecordingObserver<>();
    RxRatingBar.ratingChanges(view)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    assertEquals(0f, o.takeNext(), 0f);

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.setRating(1f);
      }
    });
    assertEquals(1f, o.takeNext(), 0f);

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.setRating(2f);
      }
    });
    assertEquals(2f, o.takeNext(), 0f);

    o.dispose();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.setRating(1f);
      }
    });
    o.assertNoMoreEvents();
  }

  @Test public void ratingChangeEvents() {
    RecordingObserver<RatingBarChangeEvent> o = new RecordingObserver<>();
    RxRatingBar.ratingChangeEvents(view) //
        .subscribeOn(AndroidSchedulers.mainThread()) //
        .subscribe(o);
    assertEquals(RatingBarChangeEvent.create(view, 0f, false), o.takeNext());

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.setRating(5f);
      }
    });
    assertEquals(RatingBarChangeEvent.create(view, 5f, false), o.takeNext());

    instrumentation.sendPointerSync(motionEventAtPosition(view, ACTION_DOWN, 0, 50));
    instrumentation.sendPointerSync(motionEventAtPosition(view, ACTION_UP, 0, 50));
    instrumentation.waitForIdleSync();
    assertEquals(RatingBarChangeEvent.create(view, 1f, true), o.takeNext());

    o.dispose();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.setRating(1f);
      }
    });
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void rating() throws Exception {
    Consumer<? super Float> action = RxRatingBar.rating(view);
    assertEquals(0f, view.getRating(), 0f);
    action.accept(1f);
    assertEquals(1f, view.getRating(), 0f);
    action.accept(2f);
    assertEquals(2f, view.getRating(), 0f);
  }

  @Test @UiThreadTest public void isIndicator() throws Exception {
    Consumer<? super Boolean> action = RxRatingBar.isIndicator(view);
    assertFalse(view.isIndicator());
    action.accept(true);
    assertTrue(view.isIndicator());
    action.accept(false);
    assertFalse(view.isIndicator());
  }
}
