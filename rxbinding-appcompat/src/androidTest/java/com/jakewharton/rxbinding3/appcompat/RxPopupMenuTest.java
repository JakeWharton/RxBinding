package com.jakewharton.rxbinding3.appcompat;

import android.app.Instrumentation;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.PopupMenu;
import androidx.test.annotation.UiThreadTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.jakewharton.rxbinding3.RecordingObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public final class RxPopupMenuTest {
  @Rule public final ActivityTestRule<RxPopupMenuTestActivity> activityRule =
      new ActivityTestRule<>(RxPopupMenuTestActivity.class);

  private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

  PopupMenu view;

  @Before public void setUp() {
    view = activityRule.getActivity().popupMenu;
  }

  @Test @UiThreadTest public void itemClicks() {
    Menu menu = view.getMenu();
    MenuItem item1 = menu.add(0, 1, 0, "Hi");
    MenuItem item2 = menu.add(0, 2, 0, "Hey");

    RecordingObserver<MenuItem> o = new RecordingObserver<>();
    com.jakewharton.rxbinding3.appcompat.RxPopupMenu.itemClicks(view).subscribe(o);
    o.assertNoMoreEvents();

    menu.performIdentifierAction(2, 0);
    assertSame(item2, o.takeNext());

    menu.performIdentifierAction(1, 0);
    assertSame(item1, o.takeNext());

    o.dispose();

    menu.performIdentifierAction(2, 0);
    o.assertNoMoreEvents();
  }

  @Test public void dismisses() {
    RecordingObserver<Object> o = new RecordingObserver<>();
    RxPopupMenu.dismisses(view)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents(); // No initial value.

    instrumentation.runOnMainSync(() -> view.show());
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(() -> view.dismiss());
    assertNotNull(o.takeNext());

    o.dispose();
    instrumentation.runOnMainSync(() -> view.show());
    instrumentation.runOnMainSync(() -> view.dismiss());

    o.assertNoMoreEvents();
  }
}
