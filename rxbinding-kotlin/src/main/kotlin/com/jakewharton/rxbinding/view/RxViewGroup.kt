package com.jakewharton.rxbinding.view

import android.view.ViewGroup
import rx.Observable

/**
 * Create an observable of hierarchy change events for `viewGroup`.
 */
public inline fun ViewGroup.changeEvents(): Observable<ViewGroupHierarchyChangeEvent> = RxViewGroup.changeEvents(this)
