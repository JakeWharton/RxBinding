package com.jakewharton.rxbinding2.support.v7.widget

import android.support.v7.widget.ActionMenuView
import android.view.MenuItem
import io.reactivex.Observable

/**
 * Create an observable which emits the clicked menu item in `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`.
 * Unsubscribe to free this reference.
 */
inline fun ActionMenuView.itemClicks(): Observable<MenuItem> = RxActionMenuView.itemClicks(this)
