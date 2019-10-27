package com.jakewharton.rxbinding3.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import androidx.test.annotation.UiThreadTest;
import androidx.test.core.app.ApplicationProvider;
import com.jakewharton.rxbinding3.RecordingObserver;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class RxViewGroupTest {
  private final Context context = ApplicationProvider.getApplicationContext();
  private final LinearLayout parent = new LinearLayout(context);
  private final View child = new View(context);

  @Test @UiThreadTest public void childViewEvents() {
    RecordingObserver<ViewGroupHierarchyChangeEvent> o = new RecordingObserver<>();
    RxViewGroup.changeEvents(parent).subscribe(o);
    o.assertNoMoreEvents(); // No initial value.

    parent.addView(child);
    assertEquals(new ViewGroupHierarchyChildViewAddEvent(parent, child), o.takeNext());

    parent.removeView(child);
    assertEquals(new ViewGroupHierarchyChildViewRemoveEvent(parent, child), o.takeNext());

    o.dispose();

    parent.addView(child);
    o.assertNoMoreEvents();
  }
}
