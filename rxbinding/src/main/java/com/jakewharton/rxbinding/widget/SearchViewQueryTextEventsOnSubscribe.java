package com.jakewharton.rxbinding.widget;

import android.widget.SearchView;

import com.jakewharton.rxbinding.internal.MainThreadSubscription;

import rx.Observable;
import rx.Subscriber;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class SearchViewQueryTextEventsOnSubscribe implements Observable.OnSubscribe<SearchViewQueryTextEvent> {
    private final boolean sendQueryTextChanges;
    private final boolean sendQueryTextSubmissions;
    private SearchView view;

    public SearchViewQueryTextEventsOnSubscribe(SearchView view) {
        this(view, true, true);
    }

    public SearchViewQueryTextEventsOnSubscribe(SearchView view,
                                                  boolean sendQueryTextChanges,
                                                  boolean sendQueryTextSubmissions) {
        this.view = view;
        this.sendQueryTextChanges = sendQueryTextChanges;
        this.sendQueryTextSubmissions = sendQueryTextSubmissions;
    }

    @Override
    public void call(final Subscriber<? super SearchViewQueryTextEvent> subscriber) {
        checkUiThread();

        final SearchView.OnQueryTextListener watcher = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String s) {
                if (sendQueryTextChanges && !subscriber.isUnsubscribed()) {
                    subscriber.onNext(new SearchViewQueryTextEvent(view, s, false));
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (sendQueryTextSubmissions && !subscriber.isUnsubscribed()) {
                    subscriber.onNext(new SearchViewQueryTextEvent(view, view.getQuery(), true ));
                    return true;
                }
                return false;
            }

        };

        subscriber.add(new MainThreadSubscription() {
            @Override
            protected void onUnsubscribe() {
                view.setOnQueryTextListener(null);
            }
        });

        view.setOnQueryTextListener(watcher);

        // Send out the initial value.
        subscriber.onNext(new SearchViewQueryTextEvent(view, view.getQuery(), false));
    }

}
