package com.jakewharton.rxbinding.support.v4.view

import android.view.MenuItem
import com.jakewharton.rxbinding.internal.Functions
import com.jakewharton.rxbinding.view.MenuItemActionViewEvent
import rx.Observable
import rx.functions.Func1

/**
 * Create an observable of action view events for `menuItem`.
 *
 * *Warning:* The created observable keeps a strong reference to `menuItem`.
 * Unsubscribe to free this reference.
 *
 * *Warning:* The created observable uses [MenuItem.setOnActionExpandListener] to
 * observe action view events. Only one observable can be used for a menu item at a time.
 */
public inline fun MenuItem.actionViewEvents(): Observable<MenuItemActionViewEvent> = RxMenuItemCompat.actionViewEvents(this)

/**
 * Create an observable of action view events for `menuItem`.
 *
 * *Warning:* The created observable keeps a strong reference to `menuItem`.
 * Unsubscribe to free this reference.
 *
 * *Warning:* The created observable uses [MenuItem.setOnActionExpandListener] to
 * observe action view events. Only one observable can be used for a menu item at a time.
 *
 * @param handled Function invoked with each value to determine the return value of the
 * underlying [MenuItem.OnActionExpandListener].
 */
public inline fun MenuItem.actionViewEvents(handled: Func1<in MenuItemActionViewEvent, Boolean>): Observable<MenuItemActionViewEvent> = RxMenuItemCompat.actionViewEvents(this, handled)
