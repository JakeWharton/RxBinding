@file:Suppress(
    names = "NOTHING_TO_INLINE"
)

package com.jakewharton.rxbinding2.support.v4.view

import android.support.annotation.CheckResult
import android.view.MenuItem
import com.jakewharton.rxbinding2.view.MenuItemActionViewEvent
import io.reactivex.Observable
import io.reactivex.functions.Predicate

/**
 * Create an observable of action view events for `menuItem`.
 *
 * *Warning:* The created observable keeps a strong reference to `menuItem`.
 * Unsubscribe to free this reference.
 *
 * *Warning:* The created observable uses [MenuItem.setOnActionExpandListener] to
 * observe action view events. Only one observable can be used for a menu item at a time.
 */
@CheckResult
inline fun MenuItem.actionViewEvents(): Observable<MenuItemActionViewEvent> = RxMenuItemCompat.actionViewEvents(this)

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
@CheckResult
inline fun MenuItem.actionViewEvents(handled: Predicate<in MenuItemActionViewEvent>): Observable<MenuItemActionViewEvent> = RxMenuItemCompat.actionViewEvents(this, handled)
