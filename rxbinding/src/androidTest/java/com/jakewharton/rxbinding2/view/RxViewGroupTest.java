package com.jakewharton.rxbinding2.view;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.LinearLayout;
import com.jakewharton.rxbinding.RecordingObserver;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public final class RxViewGroupTest {
  @Rule public final UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();

  private final Context context = InstrumentationRegistry.getTargetContext();
  private final LinearLayout parent = new LinearLayout(context);
  private final View child = new View(context);

  @Test @UiThreadTest public void childViewEvents() {
    RecordingObserver<ViewGroupHierarchyChangeEvent> o = new RecordingObserver<>();
    RxViewGroup.changeEvents(parent).subscribe(o);
    o.assertNoMoreEvents(); // No initial value.

    parent.addView(child);
    assertThat(o.takeNext()).isEqualTo(ViewGroupHierarchyChildViewAddEvent.create(parent, child));

    parent.removeView(child);
    assertThat(o.takeNext()).isEqualTo(
        ViewGroupHierarchyChildViewRemoveEvent.create(parent, child));

    o.dispose();

    parent.addView(child);
    o.assertNoMoreEvents();
  }
}
