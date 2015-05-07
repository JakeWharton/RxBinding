package rx.android.widget;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.RatingBar;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import rx.Subscription;
import rx.android.RecordingObserver;
import rx.android.schedulers.HandlerSchedulers;
import rx.functions.Action1;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;
import static com.google.common.truth.Truth.assertThat;
import static rx.android.MotionEventUtil.motionEventAtPosition;

@RunWith(AndroidJUnit4.class) public final class RxRatingBarTest {
  @Rule public final ActivityTestRule<RxRatingBarTestActivity> activityRule =
      new ActivityTestRule<>(RxRatingBarTestActivity.class);

  private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

  RatingBar view;

  @Before public void setUp() {
    view = activityRule.getActivity().ratingBar;
  }

  @Test public void ratingChanges() {
    RecordingObserver<Float> o = new RecordingObserver<>();
    Subscription subscription = RxRatingBar.ratingChanges(view) //
        .subscribeOn(HandlerSchedulers.mainThread()) //
        .subscribe(o);
    o.assertNoMoreEvents();

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

    subscription.unsubscribe();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.setRating(1f);
      }
    });
    o.assertNoMoreEvents();
  }

  @Test public void ratingChangeEvents() {
    RecordingObserver<RatingBarChangeEvent> o = new RecordingObserver<>();
    Subscription subscription = RxRatingBar.ratingChangeEvents(view) //
        .subscribeOn(HandlerSchedulers.mainThread()) //
        .subscribe(o);
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.setRating(5f);
      }
    });
    assertThat(o.takeNext()).isEqualTo(RatingBarChangeEvent.create(view, 5f, false));

    instrumentation.sendPointerSync(motionEventAtPosition(view, ACTION_DOWN, 0));
    instrumentation.sendPointerSync(motionEventAtPosition(view, ACTION_UP, 0));
    instrumentation.waitForIdleSync();
    assertThat(o.takeNext()).isEqualTo(RatingBarChangeEvent.create(view, 1f, true));

    subscription.unsubscribe();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.setRating(1f);
      }
    });
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void rating() {
    Action1<? super Float> action = RxRatingBar.rating(view);
    assertThat(view.getRating()).isEqualTo(0f);
    action.call(1f);
    assertThat(view.getRating()).isEqualTo(1f);
    action.call(2f);
    assertThat(view.getRating()).isEqualTo(2f);
  }

  @Test @UiThreadTest public void isIndicator() {
    Action1<? super Boolean> action = RxRatingBar.isIndicator(view);
    assertThat(view.isIndicator()).isFalse();
    action.call(true);
    assertThat(view.isIndicator()).isTrue();
    action.call(false);
    assertThat(view.isIndicator()).isFalse();
  }
}
