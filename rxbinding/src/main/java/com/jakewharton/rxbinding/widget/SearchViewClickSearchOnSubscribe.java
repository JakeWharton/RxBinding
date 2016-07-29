package com.jakewharton.rxbinding.widget;

import android.view.View;
import android.widget.SearchView;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static rx.android.MainThreadSubscription.verifyMainThread;

final class SearchViewClickSearchOnSubscribe implements Observable.OnSubscribe<Void> {
    private final SearchView view;

    public SearchViewClickSearchOnSubscribe(SearchView view) {
        this.view = view;
    }

    @Override public void call(final Subscriber<? super Void> subscriber) {
        verifyMainThread();

        View.OnClickListener listener = new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(null);
                }
            }
        };
        view.setOnSearchClickListener(listener);

        subscriber.add(new MainThreadSubscription() {
            @Override protected void onUnsubscribe() {
                view.setOnSearchClickListener(null);
            }
        });
    }
}
