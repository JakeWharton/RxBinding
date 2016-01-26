package com.jakewharton.rxbinding.view

import android.view.ViewGroup
import rx.Observable

/**
 * Create an observable of hierarchy change events for `viewGroup`.
 *
 * *Warning:* The created observable keeps a strong reference to `viewGroup`.
 * Unsubscribe to free this reference.
 */
public inline fun ViewGroup.changeEvents(): Observable<ViewGroupHierarchyChangeEvent> = RxViewGroup.changeEvents(this)
