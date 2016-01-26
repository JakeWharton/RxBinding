package com.jakewharton.rxbinding.support.design.widget

import android.support.design.widget.NavigationView
import android.view.MenuItem
import rx.Observable

/**
 * Create an observable which emits the selected item in `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* If an item is already selected, it will be emitted immediately on subscribe.
 * This behavior assumes but does not enforce that the items are exclusively checkable.
 */
public inline fun NavigationView.itemSelections(): Observable<MenuItem> = RxNavigationView.itemSelections(this)
