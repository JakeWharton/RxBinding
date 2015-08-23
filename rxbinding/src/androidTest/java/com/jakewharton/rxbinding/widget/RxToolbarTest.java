package com.jakewharton.rxbinding.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.filters.SdkSuppress;
import android.support.test.rule.UiThreadTestRule;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;
import com.jakewharton.rxbinding.RecordingObserver;
import org.junit.Rule;
import org.junit.Test;
import rx.Subscription;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static com.google.common.truth.Truth.assertThat;

@TargetApi(LOLLIPOP)
@SdkSuppress(minSdkVersion = LOLLIPOP)
public final class RxToolbarTest {
  @Rule public final UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();

  private final Context rawContext = InstrumentationRegistry.getContext();
  private final Context context = new ContextThemeWrapper(rawContext, android.R.style.Theme_Material);
  private final Toolbar view = new Toolbar(context);
  private final Menu menu = view.getMenu();
  private final MenuItem item1 = menu.add(0, 1, 0, "Hi");
  private final MenuItem item2 = menu.add(0, 2, 0, "Hey");

  @Test @UiThreadTest public void itemClicks() {
    RecordingObserver<MenuItem> o = new RecordingObserver<>();
    Subscription subscription = RxToolbar.itemClicks(view).subscribe(o);
    o.assertNoMoreEvents();

    menu.performIdentifierAction(2, 0);
    assertThat(o.takeNext()).isSameAs(item2);

    menu.performIdentifierAction(1, 0);
    assertThat(o.takeNext()).isSameAs(item1);

    subscription.unsubscribe();

    menu.performIdentifierAction(2, 0);
    o.assertNoMoreEvents();
  }
}
