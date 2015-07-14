package com.jakewharton.rxbinding.widget;

import android.widget.SearchView;

import com.jakewharton.rxbinding.internal.MainThreadSubscription;

import rx.Observable;
import rx.Subscriber;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

public class SearchViewQueryTextChangesOnSubscribe implements Observable.OnSubscribe<CharSequence> {
    private SearchView view;

    public SearchViewQueryTextChangesOnSubscribe(SearchView view) {
        this.view = view;
    }

    @Override
    public void call(final Subscriber<? super CharSequence> subscriber) {
        checkUiThread();

        final SearchView.OnQueryTextListener watcher = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String s) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(s);
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
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
        subscriber.onNext(view.getQuery());
    }
}
