package com.jakewharton.rxbinding.widget;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.AutoCompleteTextView;

import com.jakewharton.rxbinding.RecordingObserver;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

@RunWith(AndroidJUnit4.class)
public class RxAutoCompleteTextViewTest {
  @Rule public final ActivityTestRule<RxAutoCompleteTextViewTestActivity> activityRule =
      new ActivityTestRule<>(RxAutoCompleteTextViewTestActivity.class);

  private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

  private AutoCompleteTextView autoCompleteTextView;

  @Before public void setUp() {
    autoCompleteTextView = activityRule.getActivity().autoCompleteTextView;
  }

  @Test public void itemClickEvents() {
    RecordingObserver<AdapterViewItemClickEvent> o = new RecordingObserver<>();
    Subscription subscription = RxAutoCompleteTextView.itemClickEvents(autoCompleteTextView) //
      .subscribeOn(AndroidSchedulers.mainThread()) //
      .subscribe(o);
      o.assertNoMoreEvents();

    // Don't really know how to test it properly since performCompletion is private in AutoCompleteTextView
  }
}
