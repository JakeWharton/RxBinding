package rx.android.view;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.UiThreadTest;
import android.view.View;
import android.widget.LinearLayout;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Map;
import java.util.WeakHashMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import rx.Subscription;
import rx.android.FakeClock;
import rx.android.RecordingObserver;
import rx.android.UiThreadRule;
import rx.android.plugins.RxAndroidPlugins;
import rx.functions.Action1;

import static com.google.common.truth.Truth.assertThat;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public final class RxViewTest {
  @Rule public final UiThreadRule uiThread = UiThreadRule.createWithTimeout(10, SECONDS);

  private final FakeClock clock = new FakeClock();
  private final Context context = InstrumentationRegistry.getContext();
  private final View view = new View(context);

  @Before public void setUp() {
    RxAndroidPlugins.getInstance().reset();
    RxAndroidPlugins.getInstance().registerClockHook(clock);
  }

  @After public void tearDown() {
    RxAndroidPlugins.getInstance().reset();
  }

  @Test @UiThreadTest public void clicks() {
    RecordingObserver<Long> o = new RecordingObserver<>();
    Subscription subscription = RxView.clicks(view).subscribe(o);
    o.assertNoMoreEvents(); // No initial value.

    clock.advance(1, SECONDS);
    view.performClick();
    assertThat(o.takeNext()).isEqualTo(1000);

    clock.advance(1, SECONDS);
    view.performClick();
    assertThat(o.takeNext()).isEqualTo(2000);

    subscription.unsubscribe();

    clock.advance(1, SECONDS);
    view.performClick();
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void clickEvents() {
    RecordingObserver<ViewClickEvent> o = new RecordingObserver<>();
    Subscription subscription = RxView.clickEvents(view).subscribe(o);
    o.assertNoMoreEvents(); // No initial value.

    clock.advance(1, SECONDS);
    view.performClick();
    assertThat(o.takeNext()).isEqualTo(ViewClickEvent.create(view, 1000));

    clock.advance(1, SECONDS);
    view.performClick();
    assertThat(o.takeNext()).isEqualTo(ViewClickEvent.create(view, 2000));

    subscription.unsubscribe();

    clock.advance(1, SECONDS);
    view.performClick();
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void drags() {
    //RecordingObserver<ViewClickEvent> o = new RecordingObserver<>();
    //Subscription subscription = RxView.clickEvents(view).subscribe(o);
    //o.assertNoMoreEvents(); // No initial value.
    //
    //clock.advance(1, SECONDS);
    //view.performClick();
    //assertThat(o.takeNext()).isEqualTo(ViewClickEvent.create(view, 1000));
    //
    //clock.advance(1, SECONDS);
    //view.performClick();
    //assertThat(o.takeNext()).isEqualTo(ViewClickEvent.create(view, 2000));
    //
    //subscription.unsubscribe();
    //
    //clock.advance(1, SECONDS);
    //view.performClick();
    //o.assertNoMoreEvents();
  }

  @Test public void weakWeakHashMap() throws InterruptedException {
    Object o = new Object();

    Map<Object, String> map = new WeakHashMap<>();
    map.put("Hey", "Ho");
    map.put(o, "Hi");
    System.out.println("values: " + new ArrayList<>(map.values()));

    WeakReference<Object> ref = new WeakReference<>(o);

    for (long i = 0; true; i++) {
      System.out.println("Try " + i);
      Runtime.getRuntime().gc();
      o = null;
      if (ref.get() == o) { // Contrived comparison against null.
        break;
      }
      Thread.sleep(100);
    }

    System.out.println("values: " + new ArrayList<>(map.values()));
  }

  @Test @UiThreadTest public void focusChanges() {
    // We need a parent which can take focus from our view when it attempts to clear.
    LinearLayout parent = new LinearLayout(context);
    parent.setFocusable(true);
    parent.addView(view);

    view.setFocusable(true);

    RecordingObserver<Boolean> o = new RecordingObserver<>();
    Subscription subscription = RxView.focusChanges(view).subscribe(o);
    assertThat(o.takeNext()).isFalse();

    view.requestFocus();
    assertThat(o.takeNext()).isTrue();

    view.clearFocus();
    assertThat(o.takeNext()).isFalse();

    subscription.unsubscribe();

    view.requestFocus();
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void focusChangeEvents() {
    // We need a parent which can take focus from our view when it attempts to clear.
    LinearLayout parent = new LinearLayout(context);
    parent.setFocusable(true);
    parent.addView(view);

    view.setFocusable(true);

    RecordingObserver<ViewFocusChangeEvent> o = new RecordingObserver<>();
    Subscription subscription = RxView.focusChangeEvents(view).subscribe(o);
    assertThat(o.takeNext()).isEqualTo(ViewFocusChangeEvent.create(view, 0, false));

    clock.advance(1, SECONDS);
    view.requestFocus();
    assertThat(o.takeNext()).isEqualTo(ViewFocusChangeEvent.create(view, 1000, true));

    clock.advance(1, SECONDS);
    view.clearFocus();
    assertThat(o.takeNext()).isEqualTo(ViewFocusChangeEvent.create(view, 2000, false));

    subscription.unsubscribe();

    clock.advance(1, SECONDS);
    view.requestFocus();
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void longClicks() {
    // We need a parent because long presses delegate to the parent.
    LinearLayout parent = new LinearLayout(context) {
      @Override public boolean showContextMenuForChild(View originalView) {
        return true;
      }
    };
    parent.addView(view);

    RecordingObserver<Long> o = new RecordingObserver<>();
    Subscription subscription = RxView.longClicks(view).subscribe(o);
    o.assertNoMoreEvents(); // No initial value.

    clock.advance(1, SECONDS);
    view.performLongClick();
    assertThat(o.takeNext()).isEqualTo(1000);

    clock.advance(1, SECONDS);
    view.performLongClick();
    assertThat(o.takeNext()).isEqualTo(2000);

    subscription.unsubscribe();

    clock.advance(1, SECONDS);
    view.performLongClick();
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void longClickEvents() {
    // We need a parent because long presses delegate to the parent.
    LinearLayout parent = new LinearLayout(context) {
      @Override public boolean showContextMenuForChild(View originalView) {
        return true;
      }
    };
    parent.addView(view);

    RecordingObserver<ViewLongClickEvent> o = new RecordingObserver<>();
    Subscription subscription = RxView.longClickEvents(view).subscribe(o);
    o.assertNoMoreEvents(); // No initial value.

    clock.advance(1, SECONDS);
    view.performLongClick();
    assertThat(o.takeNext()).isEqualTo(ViewLongClickEvent.create(view, 1000));

    clock.advance(1, SECONDS);
    view.performLongClick();
    assertThat(o.takeNext()).isEqualTo(ViewLongClickEvent.create(view, 2000));

    subscription.unsubscribe();

    clock.advance(1, SECONDS);
    view.performLongClick();
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void setActivated() {
    view.setActivated(true);
    Action1<? super Boolean> action = RxView.setActivated(view);
    action.call(false);
    assertThat(view.isActivated()).isFalse();
    action.call(true);
    assertThat(view.isActivated()).isTrue();
  }

  @Test @UiThreadTest public void setClickable() {
    view.setClickable(true);
    Action1<? super Boolean> action = RxView.setClickable(view);
    action.call(false);
    assertThat(view.isClickable()).isFalse();
    action.call(true);
    assertThat(view.isClickable()).isTrue();
  }

  @Test @UiThreadTest public void setEnabled() {
    view.setEnabled(true);
    Action1<? super Boolean> action = RxView.setEnabled(view);
    action.call(false);
    assertThat(view.isEnabled()).isFalse();
    action.call(true);
    assertThat(view.isEnabled()).isTrue();
  }

  @Test @UiThreadTest public void setPressed() {
    view.setPressed(true);
    Action1<? super Boolean> action = RxView.setPressed(view);
    action.call(false);
    assertThat(view.isPressed()).isFalse();
    action.call(true);
    assertThat(view.isPressed()).isTrue();
  }

  @Test @UiThreadTest public void setSelected() {
    view.setSelected(true);
    Action1<? super Boolean> action = RxView.setSelected(view);
    action.call(false);
    assertThat(view.isSelected()).isFalse();
    action.call(true);
    assertThat(view.isSelected()).isTrue();
  }

  @Test @UiThreadTest public void setVisibility() {
    view.setVisibility(View.VISIBLE);
    Action1<? super Boolean> action = RxView.setVisibility(view);
    action.call(false);
    assertThat(view.getVisibility()).isEqualTo(View.GONE);
    action.call(true);
    assertThat(view.getVisibility()).isEqualTo(View.VISIBLE);
  }

  @Test @UiThreadTest public void setVisibilityCustomFalse() {
    view.setVisibility(View.VISIBLE);
    Action1<? super Boolean> action = RxView.setVisibility(view, View.INVISIBLE);
    action.call(false);
    assertThat(view.getVisibility()).isEqualTo(View.INVISIBLE);
    action.call(true);
    assertThat(view.getVisibility()).isEqualTo(View.VISIBLE);
  }

  @Test @UiThreadTest public void setVisibilityCustomFalseToVisibleThrows() {
    try {
      RxView.setVisibility(view, View.VISIBLE);
      fail();
    } catch (IllegalArgumentException e) {
      assertThat(e).hasMessage("Setting visibility to VISIBLE when false would have no effect.");
    }
  }
}
