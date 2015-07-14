package com.jakewharton.rxbinding.widget;

import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.SearchView;

import com.jakewharton.rxbinding.RecordingObserver;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import rx.Subscription;

import static com.google.common.truth.Truth.assertThat;

@RunWith(AndroidJUnit4.class)
public final class RxSearchViewTest {
    @Rule
    public final ActivityTestRule<RxSearchViewTestActivity>
            activityRule = new ActivityTestRule<>(RxSearchViewTestActivity.class);

    private SearchView searchView;

    @Before
    public void setUp() {
        searchView = activityRule.getActivity().searchView;
    }

    @Test @UiThreadTest public void queryTextChanges() {
        searchView.setQuery("Initial", false);
        RecordingObserver<CharSequence> o = new RecordingObserver<>();
        Subscription subscription = RxSearchView.queryTextChanges(searchView).subscribe(o);
        assertThat(o.takeNext().toString()).isEqualTo("Initial");

        searchView.setQuery("H", false);
        assertThat(o.takeNext().toString()).isEqualTo("H");
        searchView.setQuery("He", false);
        assertThat(o.takeNext().toString()).isEqualTo("He");

        searchView.setQuery(null, false); // Internally coerced to empty string.
        assertThat(o.takeNext().toString()).isEmpty();

        subscription.unsubscribe();

        searchView.setQuery("Silent", false);
        o.assertNoMoreEvents();
    }

    @Test @UiThreadTest public void querySubmissionPositive() {
        RecordingObserver<SearchViewQueryTextEvent> o = new RecordingObserver<>();
        Subscription subscription = RxSearchView.queryTextSubmissions(searchView).subscribe(o);

        searchView.setQuery("a submitted query", true);
        SearchViewQueryTextEvent event = o.takeNext();
        assertThat(event.queryText().toString()).isEqualTo("a submitted query");
        assertThat(event.isSubmitted()).isTrue();

        subscription.unsubscribe();

        searchView.setQuery("Silent", true);
        o.assertNoMoreEvents();
    }

    @Test @UiThreadTest public void querySubmissionsNegative() {
        RecordingObserver<SearchViewQueryTextEvent> o = new RecordingObserver<>();
        Subscription subscription = RxSearchView.queryTextSubmissions(searchView).subscribe(o);

        searchView.setQuery("Silent", false);
        o.assertNoMoreEvents();
    }

    @Test @UiThreadTest public void query() {
        RxSearchView.query(searchView, false).call("Hey");
        assertThat(searchView.getQuery().toString()).isEqualTo("Hey");

        RxSearchView.query(searchView, true).call("Bye");
        assertThat(searchView.getQuery().toString()).isEqualTo("Bye");
    }

    @Test @UiThreadTest public void queryTextEventNotSubmitted() {
        RecordingObserver<SearchViewQueryTextEvent> o = new RecordingObserver<>();
        Subscription subscription = RxSearchView.queryTextEvents(searchView).subscribe(o);

        searchView.setQuery("q", false);
        SearchViewQueryTextEvent event = o.takeNext();
        assertThat(event.queryText().toString()).isEqualTo("q");
        assertThat(event.isSubmitted()).isFalse();
        o.assertNoMoreEvents();
    }

    @Test @UiThreadTest public void queryTextEventSubmitted() {
        RecordingObserver<SearchViewQueryTextEvent> o = new RecordingObserver<>();
        Subscription subscription = RxSearchView.queryTextEvents(searchView).subscribe(o);

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
