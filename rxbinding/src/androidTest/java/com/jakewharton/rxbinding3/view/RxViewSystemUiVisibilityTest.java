package com.jakewharton.rxbinding3.view;

import android.app.Instrumentation;
import android.view.View;
import android.widget.FrameLayout;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.jakewharton.rxbinding3.RecordingObserver;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class RxViewSystemUiVisibilityTest {
  @Rule public final ActivityTestRule<RxViewSystemUiVisibilityTestActivity> activityRule =
      new ActivityTestRule<>(RxViewSystemUiVisibilityTestActivity.class);

  private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
  FrameLayout root;

  @Before public void setUp() {
    RxViewSystemUiVisibilityTestActivity activity = activityRule.getActivity();
    root = activity.root;
    root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
  }

  @Test public void systemUiVisibilityChanges() {
    RecordingObserver<Integer> o = new RecordingObserver<>();
    RxView.systemUiVisibilityChanges(root)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(
        () -> root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION));
    assertEquals(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION, o.takeNext().intValue());

    instrumentation.runOnMainSync(() -> root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE));
    assertEquals(View.SYSTEM_UI_FLAG_VISIBLE, o.takeNext().intValue());

    o.dispose();

    instrumentation.runOnMainSync(
        () -> root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION));
    o.assertNoMoreEvents();
  }
}
