package com.jakewharton.rxbinding2.view

import android.view.ViewGroup
import io.reactivex.Observable

/**
 * Create an observable of hierarchy change events for `viewGroup`.
 *
 * *Warning:* The created observable keeps a strong reference to `viewGroup`.
 * Unsubscribe to free this reference.
 */
inline fun ViewGroup.changeEvents(): Observable<ViewGroupHierarchyChangeEvent> = RxViewGroup.changeEvents(this)
