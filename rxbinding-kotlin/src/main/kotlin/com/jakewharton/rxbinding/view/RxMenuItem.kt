package com.jakewharton.rxbinding.view

import android.graphics.drawable.Drawable
import android.view.MenuItem
import rx.Observable
import rx.functions.Action1

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
