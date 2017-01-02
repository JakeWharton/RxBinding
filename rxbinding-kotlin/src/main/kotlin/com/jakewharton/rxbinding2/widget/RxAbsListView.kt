package com.jakewharton.rxbinding2.widget

import android.widget.AbsListView
import io.reactivex.Observable

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
inline fun AbsListView.scrollEvents(): Observable<AbsListViewScrollEvent> = RxAbsListView.scrollEvents(this)
