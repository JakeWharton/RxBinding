package com.jakewharton.rxbinding.support.v7.widget

import android.support.v7.widget.RecyclerView
import rx.Observable

/**
 * Create an observable of child attach state change events on `recyclerView`.
 *
 * *Warning:* The created observable keeps a strong reference to `recyclerView`.
 * Unsubscribe to free this reference.
 */
public inline fun RecyclerView.childAttachStateChangeEvents(): Observable<RecyclerViewChildAttachStateChangeEvent> = RxRecyclerView.childAttachStateChangeEvents(this)

/**
 * Create an observable of scroll events on `recyclerView`.
 *
 * *Warning:* The created observable keeps a strong reference to `recyclerView`.
 * Unsubscribe to free this reference.
 */
public inline fun RecyclerView.scrollEvents(): Observable<RecyclerViewScrollEvent> = RxRecyclerView.scrollEvents(this)

/**
 * Create an observable of scroll state changes on `recyclerView`.
 *
 * *Warning:* The created observable keeps a strong reference to `recyclerView`.
 * Unsubscribe to free this reference.
 */
public inline fun RecyclerView.scrollStateChanges(): Observable<Int> = RxRecyclerView.scrollStateChanges(this)
