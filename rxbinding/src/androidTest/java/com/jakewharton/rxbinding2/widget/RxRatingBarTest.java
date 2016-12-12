package com.jakewharton.rxbinding2.widget;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.RatingBar;
import com.jakewharton.rxbinding2.RecordingObserver;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;
import static com.google.common.truth.Truth.assertThat;
import static com.jakewharton.rxbinding.MotionEventUtil.motionEventAtPosition;

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
    assertThat(o.takeNext()).isEqualTo(0f);

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.setRating(1f);
      }
    });
    assertThat(o.takeNext()).isEqualTo(1f);

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.setRating(2f);
      }
    });
    assertThat(o.takeNext()).isEqualTo(2f);

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
    assertThat(o.takeNext()).isEqualTo(RatingBarChangeEvent.create(view, 0f, false));

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.setRating(5f);
      }
    });
    assertThat(o.takeNext()).isEqualTo(RatingBarChangeEvent.create(view, 5f, false));

    instrumentation.sendPointerSync(motionEventAtPosition(view, ACTION_DOWN, 0, 50));
    instrumentation.sendPointerSync(motionEventAtPosition(view, ACTION_UP, 0, 50));
    instrumentation.waitForIdleSync();
    assertThat(o.takeNext()).isEqualTo(RatingBarChangeEvent.create(view, 1f, true));

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
    assertThat(view.getRating()).isEqualTo(0f);
    action.accept(1f);
    assertThat(view.getRating()).isEqualTo(1f);
    action.accept(2f);
    assertThat(view.getRating()).isEqualTo(2f);
  }

  @Test @UiThreadTest public void isIndicator() throws Exception {
    Consumer<? super Boolean> action = RxRatingBar.isIndicator(view);
    assertThat(view.isIndicator()).isFalse();
    action.accept(true);
    assertThat(view.isIndicator()).isTrue();
    action.accept(false);
    assertThat(view.isIndicator()).isFalse();
  }
}
