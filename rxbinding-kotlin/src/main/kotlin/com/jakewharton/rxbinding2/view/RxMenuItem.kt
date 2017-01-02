package com.jakewharton.rxbinding2.view

import android.graphics.drawable.Drawable
import android.view.MenuItem
import com.jakewharton.rxbinding2.internal.Functions
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Predicate

/**
 * Create an observable which emits on `menuItem` click events. The emitted value is
 * unspecified and should only be used as notification.
 *
 * *Warning:* The created observable keeps a strong reference to `menuItem`.
 * Unsubscribe to free this reference.
 *
 * *Warning:* The created observable uses [MenuItem.setOnMenuItemClickListener] to
 * observe clicks. Only one observable can be used for a menu item at a time.
 */
inline fun MenuItem.clicks(): Observable<Any> = RxMenuItem.clicks(this)

/**
 * Create an observable which emits on `menuItem` click events. The emitted value is
 * unspecified and should only be used as notification.
 *
 * *Warning:* The created observable keeps a strong reference to `menuItem`.
 * Unsubscribe to free this reference.
 *
 * *Warning:* The created observable uses [MenuItem.setOnMenuItemClickListener] to
 * observe clicks. Only one observable can be used for a menu item at a time.
 *
 * @param handled Function invoked with each value to determine the return value of the
 * underlying [MenuItem.OnMenuItemClickListener].
 */
inline fun MenuItem.clicks(handled: Predicate<in MenuItem>): Observable<Any> = RxMenuItem.clicks(this, handled)

/**
 * Create an observable of action view events for `menuItem`.
 *
 * *Warning:* The created observable keeps a strong reference to `menuItem`.
 * Unsubscribe to free this reference.
 *
 * *Warning:* The created observable uses [MenuItem.setOnActionExpandListener] to
 * observe action view events. Only one observable can be used for a menu item at a time.
 */
inline fun MenuItem.actionViewEvents(): Observable<MenuItemActionViewEvent> = RxMenuItem.actionViewEvents(this)

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
inline fun MenuItem.actionViewEvents(handled: Predicate<in MenuItemActionViewEvent>): Observable<MenuItemActionViewEvent> = RxMenuItem.actionViewEvents(this, handled)

/**
 * An action which sets the checked property of `menuItem`.
 *
 * *Warning:* The created observable keeps a strong reference to `menuItem`.
 * Unsubscribe to free this reference.
 */
inline fun MenuItem.checked(): Consumer<in Boolean> = RxMenuItem.checked(this)

/**
 * An action which sets the enabled property of `menuItem`.
 *
 * *Warning:* The created observable keeps a strong reference to `menuItem`.
 * Unsubscribe to free this reference.
 */
inline fun MenuItem.enabled(): Consumer<in Boolean> = RxMenuItem.enabled(this)

/**
 * An action which sets the icon property of `menuItem`.
 *
 * *Warning:* The created observable keeps a strong reference to `menuItem`.
 * Unsubscribe to free this reference.
 */
inline fun MenuItem.icon(): Consumer<in Drawable> = RxMenuItem.icon(this)

/**
 * An action which sets the icon property of `menuItem`.
 *
 * *Warning:* The created observable keeps a strong reference to `menuItem`.
 * Unsubscribe to free this reference.
 */
inline fun MenuItem.iconRes(): Consumer<in Int> = RxMenuItem.iconRes(this)

/**
 * An action which sets the title property of `menuItem`.
 *
 * *Warning:* The created observable keeps a strong reference to `menuItem`.
 * Unsubscribe to free this reference.
 */
inline fun MenuItem.title(): Consumer<in CharSequence> = RxMenuItem.title(this)

/**
 * An action which sets the title property of `menuItem`.
 *
 * *Warning:* The created observable keeps a strong reference to `menuItem`.
 * Unsubscribe to free this reference.
 */
inline fun MenuItem.titleRes(): Consumer<in Int> = RxMenuItem.titleRes(this)

/**
 * An action which sets the visibility property of `menuItem`.
 *
 * *Warning:* The created observable keeps a strong reference to `menuItem`.
 * Unsubscribe to free this reference.
 */
inline fun MenuItem.visible(): Consumer<in Boolean> = RxMenuItem.visible(this)
