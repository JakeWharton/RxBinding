package com.jakewharton.rxbinding2.widget;

import android.content.Context;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import androidx.test.InstrumentationRegistry;
import androidx.test.annotation.UiThreadTest;
import com.jakewharton.rxbinding2.RecordingObserver;
import io.reactivex.functions.Consumer;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class RxCompoundButtonTest {
  private final Context context = InstrumentationRegistry.getContext();
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
