package com.jakewharton.rxbinding2.support.design.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.filters.SdkSuppress;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.ContextThemeWrapper;
import android.view.View;
import com.jakewharton.rxbinding2.RecordingObserver;
import com.jakewharton.rxbinding2.support.design.R;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.google.common.truth.Truth.assertThat;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;

@RunWith(AndroidJUnit4.class)
public class RxAppBarLayoutTest {
  @Rule public final UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();

  private final Context rawContext = InstrumentationRegistry.getContext();
  private final Context context = new ContextThemeWrapper(rawContext, R.style.Theme_AppCompat);
  private final CoordinatorLayout parent = new CoordinatorLayout(context);
  private final AppBarLayout view = new AppBarLayout(context);

  @Before public void setUp() {
    parent.addView(view);
  }

  @TargetApi(JELLY_BEAN_MR1)
  @SdkSuppress(minSdkVersion = JELLY_BEAN_MR1)
  @Test @UiThreadTest public void offsetChanges() {
    RecordingObserver<Integer> o = new RecordingObserver<>();
    RxAppBarLayout.offsetChanges(view).subscribe(o);
    o.assertNoMoreEvents();

    CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
    AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
    params.setBehavior(behavior);
    behavior.onLayoutChild(parent, view, View.LAYOUT_DIRECTION_LTR);
    assertThat(o.takeNext()).isEqualTo(0);

    o.dispose();

    behavior.onLayoutChild(parent, view, View.LAYOUT_DIRECTION_LTR);
    o.assertNoMoreEvents();
  }
}
