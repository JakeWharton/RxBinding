package com.jakewharton.rxbinding4.widget;

import android.content.Context;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import androidx.test.annotation.UiThreadTest;
import androidx.test.core.app.ApplicationProvider;
import com.jakewharton.rxbinding4.RecordingObserver;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class RxCompoundButtonTest {
  private final Context context = ApplicationProvider.getApplicationContext();
  private final CompoundButton view = new ToggleButton(context);

  @Test @UiThreadTest public void checkedChanges() {
    view.setChecked(false);

    RecordingObserver<Boolean> o = new RecordingObserver<>();
    RxCompoundButton.checkedChanges(view).subscribe(o);
    assertFalse(o.takeNext());

    view.setChecked(true);
    assertTrue(o.takeNext());
    view.setChecked(false);
    assertFalse(o.takeNext());

    o.dispose();

    view.setChecked(true);
    o.assertNoMoreEvents();
  }
}
