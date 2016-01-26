package com.jakewharton.rxbinding.support.v7.widget

import android.support.v7.widget.Toolbar
import android.view.MenuItem
import rx.Observable

/**
 * Create an observable which emits the clicked item in `view`'s menu.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun Toolbar.itemClicks(): Observable<MenuItem> = RxToolbar.itemClicks(this)

/**
 * Create an observable which emits on `view` navigation click events. The emitted value is
 * unspecified and should only be used as notification.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [Toolbar.setNavigationOnClickListener]
 * to observe clicks. Only one observable can be used for a view at a time.
 */
public inline fun Toolbar.navigationClicks(): Observable<Unit> = RxToolbar.navigationClicks(this).map { Unit }
