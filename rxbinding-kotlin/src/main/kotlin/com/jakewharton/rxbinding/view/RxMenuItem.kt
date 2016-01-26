package com.jakewharton.rxbinding.view

import android.graphics.drawable.Drawable
import android.view.MenuItem
import com.jakewharton.rxbinding.internal.Functions
import rx.Observable
import rx.functions.Action1
import rx.functions.Func1

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
public inline fun MenuItem.clicks(): Observable<Unit> = RxMenuItem.clicks(this).map { Unit }

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
public inline fun MenuItem.clicks(handled: Func1<in MenuItem, Boolean>): Observable<Unit> = RxMenuItem.clicks(this, handled).map { Unit }

/**
 * Create an observable of action view events for `menuItem`.
 *
 * *Warning:* The created observable keeps a strong reference to `menuItem`.
 * Unsubscribe to free this reference.
 *
 * *Warning:* The created observable uses [MenuItem.setOnActionExpandListener] to
 * observe action view events. Only one observable can be used for a menu item at a time.
 */
public inline fun MenuItem.actionViewEvents(): Observable<MenuItemActionViewEvent> = RxMenuItem.actionViewEvents(this)

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
public inline fun MenuItem.actionViewEvents(handled: Func1<in MenuItemActionViewEvent, Boolean>): Observable<MenuItemActionViewEvent> = RxMenuItem.actionViewEvents(this, handled)

/**
 * An action which sets the checked property of `menuItem`.
 *
 * *Warning:* The created observable keeps a strong reference to `menuItem`.
 * Unsubscribe to free this reference.
 */
public inline fun MenuItem.checked(): Action1<in Boolean> = RxMenuItem.checked(this)

/**
 * An action which sets the enabled property of `menuItem`.
 *
 * *Warning:* The created observable keeps a strong reference to `menuItem`.
 * Unsubscribe to free this reference.
 */
public inline fun MenuItem.enabled(): Action1<in Boolean> = RxMenuItem.enabled(this)

/**
 * An action which sets the icon property of `menuItem`.
 *
 * *Warning:* The created observable keeps a strong reference to `menuItem`.
 * Unsubscribe to free this reference.
 */
public inline fun MenuItem.icon(): Action1<in Drawable> = RxMenuItem.icon(this)

/**
 * An action which sets the icon property of `menuItem`.
 *
 * *Warning:* The created observable keeps a strong reference to `menuItem`.
 * Unsubscribe to free this reference.
 */
public inline fun MenuItem.iconRes(): Action1<in Int> = RxMenuItem.iconRes(this)

/**
 * An action which sets the title property of `menuItem`.
 *
 * *Warning:* The created observable keeps a strong reference to `menuItem`.
 * Unsubscribe to free this reference.
 */
public inline fun MenuItem.title(): Action1<in CharSequence> = RxMenuItem.title(this)

/**
 * An action which sets the title property of `menuItem`.
 *
 * *Warning:* The created observable keeps a strong reference to `menuItem`.
 * Unsubscribe to free this reference.
 */
public inline fun MenuItem.titleRes(): Action1<in Int> = RxMenuItem.titleRes(this)

/**
 * An action which sets the visibility property of `menuItem`.
 *
 * *Warning:* The created observable keeps a strong reference to `menuItem`.
 * Unsubscribe to free this reference.
 */
public inline fun MenuItem.visible(): Action1<in Boolean> = RxMenuItem.visible(this)
