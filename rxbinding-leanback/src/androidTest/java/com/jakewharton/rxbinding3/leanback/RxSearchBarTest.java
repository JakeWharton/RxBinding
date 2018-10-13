package com.jakewharton.rxbinding3.leanback;

import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.KeyEvent;
import androidx.leanback.widget.SearchBar;
import androidx.leanback.widget.SearchEditText;
import com.jakewharton.rxbinding2.RecordingObserver;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

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
    assertEquals("H", o.takeNext());
    searchBar.setSearchQuery("He");
    assertEquals("He", o.takeNext());

    o.dispose();

    searchBar.setSearchQuery("Silent");
    o.assertNoMoreEvents();
  }

  @Test @UiThreadTest public void searchQueryChangeEvents() {
    RecordingObserver<SearchBarSearchQueryEvent> o = new RecordingObserver<>();
    RxSearchBar.searchQueryChangeEvents(searchBar).subscribe(o);
    o.assertNoMoreEvents();

    searchBar.setSearchQuery("q");
    assertEquals(new SearchBarSearchQueryChangedEvent(searchBar, "q"), o.takeNext());
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

    assertEquals(new SearchBarSearchQueryChangedEvent(searchBar, "q"), o.takeNext());
    assertEquals(new SearchBarSearchQueryKeyboardDismissedEvent(searchBar, "q"), o.takeNext());

    o.dispose();
    searchEditText.onKeyPreIme(KeyEvent.KEYCODE_BACK, keyEvent);
    o.assertNoMoreEvents();
  }
}
