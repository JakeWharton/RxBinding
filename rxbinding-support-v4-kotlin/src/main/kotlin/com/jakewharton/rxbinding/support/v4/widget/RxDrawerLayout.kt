package com.jakewharton.rxbinding.support.v4.widget

import android.support.v4.widget.DrawerLayout
import rx.Observable
import rx.functions.Action1

/**
 * Create an observable of the open state of the drawer of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
public inline fun DrawerLayout.drawerOpen(gravity: Int): Observable<Boolean> = RxDrawerLayout.drawerOpen(this, gravity)

/**
 * An action which sets whether the drawer with `gravity` of `view` is open.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun DrawerLayout.open(gravity: Int): Action1<in Boolean> = RxDrawerLayout.open(this, gravity)
