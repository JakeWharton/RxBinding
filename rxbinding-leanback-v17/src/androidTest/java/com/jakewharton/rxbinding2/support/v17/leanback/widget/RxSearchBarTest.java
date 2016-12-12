package com.jakewharton.rxbinding2.support.v17.leanback.widget;

import com.jakewharton.rxbinding2.RecordingObserver;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v17.leanback.widget.SearchBar;
import android.support.v17.leanback.widget.SearchEditText;
import android.view.KeyEvent;

import static com.google.common.truth.Truth.assertThat;
import static com.jakewharton.rxbinding2.support.v17.leanback.widget.SearchBarSearchQueryEvent.Kind.CHANGED;
import static com.jakewharton.rxbinding2.support.v17.leanback.widget.SearchBarSearchQueryEvent.Kind.KEYBOARD_DISMISSED;

@RunWith(AndroidJUnit4.class)
public final class RxSearchBarTest {
  @Rule public final ActivityTestRule<RxSearchBarTestActivity> activityRule =
          new ActivityTestRule<>(RxSearchBarTestActivity.class);

  private SearchBar searchBar;
  private SearchEditText searchEditText;

  @Before public void setUp() {
    RxSearchBarTestActivity activity = activityRule.getActivity();
    searchBar = activity.searchBar;
    searchEditText = activity.searchEditText;
  }

  @Test @UiThreadTest public void searchQueryChanges() {
    RecordingObserver<String> o = new RecordingObserver<>();
    RxSearchBar.searchQueryChanges(searchBar).subscribe(o);
    o.assertNoMoreEvents();

    searchBar.setSearchQuery("H");
    assertThat(o.takeNext()).isEqualTo("H");
    searchBar.setSearchQuery("He");
    assertThat(o.takeNext()).isEqualTo("He");

    o.dispose();

    searchBar.setSearchQuery("Silent");
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void searchQuery() throws Exception {
    RxSearchBar.searchQuery(searchBar).accept("Hey");
    assertThat(searchEditText.getText().toString()).isEqualTo("Hey");

    RxSearchBar.searchQuery(searchBar).accept("Bye");
    assertThat(searchEditText.getText().toString()).isEqualTo("Bye");
  }

  @Test @UiThreadTest public void searchQueryChangeEvents() {
    RecordingObserver<SearchBarSearchQueryEvent> o = new RecordingObserver<>();
    RxSearchBar.searchQueryChangeEvents(searchBar).subscribe(o);
    o.assertNoMoreEvents();

    searchBar.setSearchQuery("q");
    SearchBarSearchQueryEvent event = o.takeNext();
    assertThat(event.searchQuery()).isEqualTo("q");
    assertThat(event.kind()).isEqualTo(CHANGED);
    o.assertNoMoreEvents();

    o.dispose();
    searchBar.setSearchQuery("Silent");
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void searchQueryChangeEventsKeyboardDismissed() {
    RecordingObserver<SearchBarSearchQueryEvent> o = new RecordingObserver<>();
    RxSearchBar.searchQueryChangeEvents(searchBar).subscribe(o);
    o.assertNoMoreEvents();

    KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);

    searchBar.setSearchQuery("q");
    searchEditText.onKeyPreIme(KeyEvent.KEYCODE_BACK, keyEvent);

    // Text change event:
    SearchBarSearchQueryEvent event = o.takeNext();
    assertThat(event.searchQuery()).isEqualTo("q");
    assertThat(event.kind()).isEqualTo(CHANGED);
    // Keyboard dismiss event:
    SearchBarSearchQueryEvent event1 = o.takeNext();
    assertThat(event1.searchQuery()).isEqualTo("q");
    assertThat(event1.kind()).isEqualTo(KEYBOARD_DISMISSED);

    o.dispose();
    searchEditText.onKeyPreIme(KeyEvent.KEYCODE_BACK, keyEvent);
    o.assertNoMoreEvents();
  }
}
