package com.jakewharton.rxbinding.widget;

import android.widget.SearchView;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Static factory methods for creating {@linkplain Observable observables} and {@linkplain Action1
 * actions} for {@link SearchView}.
 */
public final class RxSearchView {
    /**
     * Create an observable of {@linkplain SearchViewQueryTextEvent query text events}
     * on {@code view}.
     * <p>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}.
     * Unsubscribe to free this reference.
     */
    public static Observable<SearchViewQueryTextEvent> queryTextEvents(SearchView view) {
        checkNotNull(view, "view == null");
        return Observable.create(new SearchViewQueryTextEventsOnSubscribe(view));
    }

    /**
     * Create an observable of character sequences for query text changes on {@code view}.
     * <p>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}.
     * Unsubscribe to free this reference.
     */

    public static Observable<? extends CharSequence> queryTextChanges(SearchView view) {
        checkNotNull(view, "view == null");
        return Observable.create(new SearchViewQueryTextChangesOnSubscribe(view));
    }

    /**
     * Create an observable of {@linkplain SearchViewQueryTextEvent query text events}
     * for queries that are submitted on {@code view}.
     * <p>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}.
     * Unsubscribe to free this reference.
     */
    public static Observable<SearchViewQueryTextEvent> queryTextSubmissions(SearchView view) {
        checkNotNull(view, "view == null");
        return RxSearchView.queryTextEvents(view)
                .filter(new Func1<SearchViewQueryTextEvent, Boolean>() {
                    @Override
                    public Boolean call(SearchViewQueryTextEvent searchViewQueryTextEvent) {
                        return searchViewQueryTextEvent.isSubmitted();
                    }
                });
    }

    /**
     * An action which sets the query property of {@code view} with character sequences.
     * <p>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}.
     * Unsubscribe to free this reference.
     *
     * @param submit weather to submit query right after updating query text
     */
    public static Action1<? super CharSequence> query(final SearchView view, final boolean submit) {
        checkNotNull(view, "view == null");
        return new Action1<CharSequence>() {
            @Override public void call(CharSequence text) {
                view.setQuery(text, submit);
            }
        };
    }

    private RxSearchView() {
        throw new AssertionError("No instances.");
    }
}
