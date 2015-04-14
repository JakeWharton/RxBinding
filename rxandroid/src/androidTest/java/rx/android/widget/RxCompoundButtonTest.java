package rx.android.widget;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.UiThreadTest;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
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

@RunWith(AndroidJUnit4.class)
public final class RxCompoundButtonTest {
  @Rule public final UiThreadRule uiThread = UiThreadRule.createWithTimeout(10, SECONDS);

  private final FakeClock clock = new FakeClock();
  private final Context context = InstrumentationRegistry.getContext();
  private final CompoundButton view = new ToggleButton(context);

  @Before public void setUp() {
    RxAndroidPlugins.getInstance().reset();
    RxAndroidPlugins.getInstance().registerClockHook(clock);
  }

  @After public void tearDown() {
    RxAndroidPlugins.getInstance().reset();
  }

  @Test @UiThreadTest public void checkedChanges() {
    view.setChecked(false);

    RecordingObserver<Boolean> o = new RecordingObserver<>();
    Subscription subscription = RxCompoundButton.checkedChanges(view).subscribe(o);
    assertThat(o.takeNext()).isFalse();

    view.setChecked(true);
    assertThat(o.takeNext()).isTrue();
    view.setChecked(false);
    assertThat(o.takeNext()).isFalse();

    subscription.unsubscribe();

    view.setChecked(true);
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void checkedChangeEvents() {
    view.setChecked(false);

    RecordingObserver<CompoundButtonCheckedChangeEvent> o = new RecordingObserver<>();
    Subscription subscription = RxCompoundButton.checkedChangeEvents(view).subscribe(o);
    CompoundButtonCheckedChangeEvent event0 = o.takeNext();
    assertThat(event0.view()).isSameAs(view);
    assertThat(event0.timestamp()).isEqualTo(0);
    assertThat(event0.isChecked()).isFalse();

    clock.advance(1, SECONDS);
    view.setChecked(true);
    CompoundButtonCheckedChangeEvent event1 = o.takeNext();
    assertThat(event1.view()).isSameAs(view);
    assertThat(event1.timestamp()).isEqualTo(1000);
    assertThat(event1.isChecked()).isTrue();

    clock.advance(1, SECONDS);
    view.setChecked(false);
    CompoundButtonCheckedChangeEvent event2 = o.takeNext();
    assertThat(event2.view()).isSameAs(view);
    assertThat(event2.timestamp()).isEqualTo(2000);
    assertThat(event2.isChecked()).isFalse();

    subscription.unsubscribe();

    clock.advance(1, SECONDS);
    view.setChecked(true);
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void setChecked() {
    view.setChecked(false);
    Action1<? super Boolean> toggle = RxCompoundButton.setChecked(view);

    toggle.call(true);
    assertThat(view.isChecked()).isTrue();

    toggle.call(false);
    assertThat(view.isChecked()).isFalse();
  }

  @Test @UiThreadTest public void toggle() {
    view.setChecked(false);
    Action1<? super Object> toggle = RxCompoundButton.toggle(view);

    toggle.call(null);
    assertThat(view.isChecked()).isTrue();

    toggle.call("OMG TOGGLES");
    assertThat(view.isChecked()).isFalse();
  }
}
