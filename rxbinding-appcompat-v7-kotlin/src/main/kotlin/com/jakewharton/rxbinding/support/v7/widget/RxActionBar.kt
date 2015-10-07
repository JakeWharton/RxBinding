package com.jakewharton.rxbinding.support.v7.widget

import android.support.v7.app.ActionBar
import rx.Observable
import rx.functions.Action1

/**
 * Create an observable of menu visibility change events in `view`.
 * 
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun ActionBar.menuVisibilityChanges(): Observable<Boolean> = RxActionBar.menuVisibilityChanges(this)
