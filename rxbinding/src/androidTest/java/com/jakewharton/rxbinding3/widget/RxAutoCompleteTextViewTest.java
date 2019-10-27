package com.jakewharton.rxbinding3.widget;

import android.app.Instrumentation;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.jakewharton.rxbinding3.RecordingObserver;
import com.jakewharton.rxbinding3.test.R;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public final class RxAutoCompleteTextViewTest {
  @Rule public final ActivityTestRule<RxAutoCompleteTextViewTestActivity> activityRule =
      new ActivityTestRule<>(RxAutoCompleteTextViewTestActivity.class);

  private final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

  private RxAutoCompleteTextViewTestActivity activity;
  AutoCompleteTextView autoCompleteTextView;

  @Before public void setUp() {
    activity = activityRule.getActivity();
    autoCompleteTextView = activity.autoCompleteTextView;
  }

  @Test public void itemClickEvents() {
    instrumentation.runOnMainSync(() -> {
      autoCompleteTextView.setThreshold(1);

      List<String> values = Arrays.asList("Two", "Three", "Twenty");
      ArrayAdapter<String> adapter = new ArrayAdapter<>(autoCompleteTextView.getContext(),
          android.R.layout.simple_list_item_1, values);
      autoCompleteTextView.setAdapter(adapter);
    });

    RecordingObserver<AdapterViewItemClickEvent> o = new RecordingObserver<>();
    RxAutoCompleteTextView.itemClickEvents(autoCompleteTextView)
      .subscribeOn(AndroidSchedulers.mainThread())
      .subscribe(o);
    o.assertNoMoreEvents();

    onView(withId(R.id.auto_complete)).perform(typeText("Tw"));
    onData(startsWith("Twenty"))
        .inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))))
        .perform(click());

    AdapterViewItemClickEvent event = o.takeNext();
    assertNotNull(event.getView());
    assertNotNull(event.getClickedView());
    assertEquals(1, event.getPosition()); // Second item in two-item filtered list.
    assertEquals(1, event.getId()); // Second item in two-item filtered list.

    o.dispose();

    onView(withId(R.id.auto_complete)).perform(clearText(), typeText("Tw"));
    onData(startsWith("Twenty"))
        .inRoot(withDecorView(not(is(activity.getWindow().getDecorView()))))
        .perform(click());

    o.assertNoMoreEvents();
  }
}
