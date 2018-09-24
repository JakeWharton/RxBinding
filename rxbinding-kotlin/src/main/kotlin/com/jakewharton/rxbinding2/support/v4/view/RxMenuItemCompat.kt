@file:Suppress("NOTHING_TO_INLINE")

package com.jakewharton.rxbinding2.support.v4.view

import android.support.annotation.CheckResult
import android.view.MenuItem
import com.jakewharton.rxbinding2.view.MenuItemActionViewEvent
import io.reactivex.Observable
import io.reactivex.functions.Predicate
import kotlin.Deprecated
import kotlin.Suppress

/**
 *
 */
@Deprecated("Use {@link RxMenuItem#actionViewEvents(MenuItem)}.")
@CheckResult
inline fun MenuItem.actionViewEvents(): Observable<MenuItemActionViewEvent> = RxMenuItemCompat.actionViewEvents(this)

/**
 *
 */
@Deprecated("Use {@link RxMenuItem#actionViewEvents(MenuItem, Predicate)}.")
@CheckResult
inline fun MenuItem.actionViewEvents(handled: Predicate<in MenuItemActionViewEvent>): Observable<MenuItemActionViewEvent> = RxMenuItemCompat.actionViewEvents(this, handled)
