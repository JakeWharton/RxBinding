package com.jakewharton.rxbinding3.widget;

import android.widget.SearchView;
import androidx.test.annotation.UiThreadTest;
import androidx.test.rule.ActivityTestRule;
import com.jakewharton.rxbinding3.RecordingObserver;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class RxSearchViewTest {
  @Rule
  public final ActivityTestRule<RxSearchViewTestActivity> activityRule =
          new ActivityTestRule<>(RxSearchViewTestActivity.class);

  private SearchView searchView;

  @Before public void setUp() {
    searchView = activityRule.getActivity().searchView;
  }

  @Test @UiThreadTest public void queryTextChanges() {
    searchView.setQuery("Initial", false);
    RecordingObserver<CharSequence> o = new RecordingObserver<>();
    RxSearchView.queryTextChanges(searchView).subscribe(o);
    assertEquals("Initial", o.takeNext().toString());

    searchView.setQuery("H", false);
    assertEquals("H", o.takeNext().toString());
    searchView.setQuery("He", false);
    assertEquals("He", o.takeNext().toString());

    searchView.setQuery(null, false); // Internally coerced to empty string.
    assertEquals("", o.takeNext().toString());

    o.dispose();

    searchView.setQuery("Silent", false);
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void query() throws Throwable {
    RxSearchView.query(searchView, false).accept("Hey");
    assertEquals("Hey", searchView.getQuery().toString());

    RxSearchView.query(searchView, true).accept("Bye");
    assertEquals("Bye", searchView.getQuery().toString());
  }

  @Test @UiThreadTest public void queryTextEventNotSubmitted() {
    RecordingObserver<SearchViewQueryTextEvent> o = new RecordingObserver<>();
    RxSearchView.queryTextChangeEvents(searchView).subscribe(o);

    assertEquals("", o.takeNext().getQueryText().toString());

    searchView.setQuery("q", false);
    SearchViewQueryTextEvent event = o.takeNext();
    assertEquals("q", event.getQueryText().toString());
    assertFalse(event.isSubmitted());
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void queryTextEventSubmitted() {
    RecordingObserver<SearchViewQueryTextEvent> o = new RecordingObserver<>();
    RxSearchView.queryTextChangeEvents(searchView).subscribe(o);

    assertEquals("", o.takeNext().getQueryText().toString());

    searchView.setQuery("q", true);
    // Text change event:
    SearchViewQueryTextEvent event = o.takeNext();
    assertEquals("q", event.getQueryText().toString());
    assertFalse(event.isSubmitted());
    // Submission event:
    SearchViewQueryTextEvent event1 = o.takeNext();
    assertEquals("q", event1.getQueryText().toString());
    assertTrue(event1.isSubmitted());
  }
}
