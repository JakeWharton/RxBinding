package com.jakewharton.rxbinding.support.design.widget;

import android.app.Instrumentation;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.ContextThemeWrapper;
import android.widget.FrameLayout;
import com.jakewharton.rxbinding.RecordingObserver;
import com.jakewharton.rxbinding.support.design.R;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static android.support.design.widget.Snackbar.Callback.DISMISS_EVENT_MANUAL;
import static android.support.design.widget.Snackbar.LENGTH_SHORT;
import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public final class RxSnackbarTest {
  private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
  private final Context rawContext = InstrumentationRegistry.getContext();
  private final Context context = new ContextThemeWrapper(rawContext, R.style.Theme_AppCompat);
  private final FrameLayout parent = new FrameLayout(context);

  @Test public void dismisses() {
    final Snackbar view = Snackbar.make(parent, "Hey", LENGTH_SHORT);

    RecordingObserver<Integer> o = new RecordingObserver<>();
    Subscription subscription = RxSnackbar.dismisses(view)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(o);
    o.assertNoMoreEvents();

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.show();
      }
    });
    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.dismiss();
      }
    });
    assertThat(o.takeNext()).isEqualTo(DISMISS_EVENT_MANUAL);

    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.show();
      }
    });
    subscription.unsubscribe();
    instrumentation.runOnMainSync(new Runnable() {
      @Override public void run() {
        view.dismiss();
      }
    });
    o.assertNoMoreEvents();
  }
}
