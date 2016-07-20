package com.jakewharton.rxbinding.widget

import android.widget.AbsListView
import rx.Observable

/**
 * Create an observable of scroll events on `absListView`.
 *
 * *Warning:* The created observable keeps a strong reference to `absListView`.
 * Unsubscribe to free this reference.
 *
 * *Warning:* The created observable uses
 * {@link AbsListView#setOnScrollListener(AbsListView.OnScrollListener)} to observe scroll
 * changes. Only one observable can be used for a view at a time.
 */
public inline fun AbsListView.scrollEvents(): Observable<AbsListViewScrollEvent> = RxAbsListView.scrollEvents(this)
