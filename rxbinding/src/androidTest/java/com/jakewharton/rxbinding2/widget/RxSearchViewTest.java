package com.jakewharton.rxbinding2.widget;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.SearchView;
import com.jakewharton.rxbinding2.RecordingObserver;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public final class RxSearchViewTest {
  @Rule public final UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();

  private final Context context = InstrumentationRegistry.getContext();

  private SearchView searchView;

  @Before public void setUp() {
    searchView = new SearchView(context);
  }

  @Test @UiThreadTest public void queryTextChanges() {
    searchView.setQuery("Initial", false);
    RecordingObserver<CharSequence> o = new RecordingObserver<>();
    RxSearchView.queryTextChanges(searchView).subscribe(o);
    assertThat(o.takeNext().toString()).isEqualTo("Initial");

    searchView.setQuery("H", false);
    assertThat(o.takeNext().toString()).isEqualTo("H");
    searchView.setQuery("He", false);
    assertThat(o.takeNext().toString()).isEqualTo("He");

    searchView.setQuery(null, false); // Internally coerced to empty string.
    assertThat(o.takeNext().toString()).isEmpty();

    o.dispose();

    searchView.setQuery("Silent", false);
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void query() throws Exception {
    RxSearchView.query(searchView, false).accept("Hey");
    assertThat(searchView.getQuery().toString()).isEqualTo("Hey");

    RxSearchView.query(searchView, true).accept("Bye");
    assertThat(searchView.getQuery().toString()).isEqualTo("Bye");
  }

  @Test @UiThreadTest public void queryTextEventNotSubmitted() {
    RecordingObserver<SearchViewQueryTextEvent> o = new RecordingObserver<>();
    RxSearchView.queryTextChangeEvents(searchView).subscribe(o);

    assertThat(o.takeNext().queryText().toString()).isEmpty();

    searchView.setQuery("q", false);
    SearchViewQueryTextEvent event = o.takeNext();
    assertThat(event.queryText().toString()).isEqualTo("q");
    assertThat(event.isSubmitted()).isFalse();
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void queryTextEventSubmitted() {
    RecordingObserver<SearchViewQueryTextEvent> o = new RecordingObserver<>();
    RxSearchView.queryTextChangeEvents(searchView).subscribe(o);

    assertThat(o.takeNext().queryText().toString()).isEmpty();

    searchView.setQuery("q", true);
    // Text change event:
    SearchViewQueryTextEvent event = o.takeNext();
    assertThat(event.queryText().toString()).isEqualTo("q");
    assertThat(event.isSubmitted()).isFalse();
    // Submission event:
    SearchViewQueryTextEvent event1 = o.takeNext();
    assertThat(event1.queryText().toString()).isEqualTo("q");
    assertThat(event1.isSubmitted()).isTrue();
  }
}
