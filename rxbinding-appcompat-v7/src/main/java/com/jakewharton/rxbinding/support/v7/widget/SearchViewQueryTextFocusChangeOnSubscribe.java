package com.jakewharton.rxbinding.support.v7.widget;

import android.support.v7.widget.SearchView;
import android.view.View;

import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

final class SearchViewQueryTextFocusChangeOnSubscribe
        implements Observable.OnSubscribe<Boolean> {
    final SearchView view;

    SearchViewQueryTextFocusChangeOnSubscribe(SearchView view) {
        this.view = view;
    }

    @Override public void call(final Subscriber<? super Boolean> subscriber) {
        View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override public void onFocusChange(View v, boolean hasFocus) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(hasFocus);
                }
            }
        };

        view.setOnQueryTextFocusChangeListener(onFocusChangeListener);

        subscriber.add(new MainThreadSubscription() {
            @Override protected void onUnsubscribe() {
                view.setOnQueryTextFocusChangeListener(null);
            }
        });
    }
}
