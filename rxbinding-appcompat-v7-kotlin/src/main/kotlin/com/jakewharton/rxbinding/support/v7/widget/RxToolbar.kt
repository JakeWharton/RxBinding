package com.jakewharton.rxbinding.support.v7.widget

import android.support.v7.widget.Toolbar
import android.view.MenuItem
import rx.Observable
import rx.functions.Action1

/**
 * Create an observable which emits the clicked item in `view`'s menu.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun Toolbar.itemClicks(): Observable<MenuItem> = RxToolbar.itemClicks(this)

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
inline fun Toolbar.navigationClicks(): Observable<Unit> = RxToolbar.navigationClicks(this).map { Unit }

/**
 * An action which sets the title property of `view` with character sequences.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun Toolbar.title(): Action1<in CharSequence> = RxToolbar.title(this)

/**
 * An action which sets the title property of `view` string resource IDs.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun Toolbar.titleRes(): Action1<in Int> = RxToolbar.titleRes(this)

/**
 * An action which sets the subtitle property of `view` with character sequences.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun Toolbar.subtitle(): Action1<in CharSequence> = RxToolbar.subtitle(this)

/**
 * An action which sets the subtitle property of `view` string resource IDs.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun Toolbar.subtitleRes(): Action1<in Int> = RxToolbar.subtitleRes(this)
