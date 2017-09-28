@file:Suppress(
    names = "NOTHING_TO_INLINE"
)

package com.jakewharton.rxbinding2.support.v4.widget

import android.support.annotation.CheckResult
import android.support.v4.widget.DrawerLayout
import com.jakewharton.rxbinding2.InitialValueObservable
import io.reactivex.functions.Consumer

/**
 * Create an observable of the open state of the drawer of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
inline fun DrawerLayout.drawerOpen(gravity: Int): InitialValueObservable<Boolean> = RxDrawerLayout.drawerOpen(this, gravity)

/**
 * An action which sets whether the drawer with `gravity` of `view` is open.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
inline fun DrawerLayout.open(gravity: Int): Consumer<in Boolean> = RxDrawerLayout.open(this, gravity)
