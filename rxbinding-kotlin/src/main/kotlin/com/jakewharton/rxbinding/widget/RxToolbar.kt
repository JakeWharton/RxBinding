package com.jakewharton.rxbinding.widget

import android.widget.Toolbar
import android.view.MenuItem
import rx.Observable
import rx.functions.Action1

/**
 * Create an observable which emits the clicked item in `view`'s menu.
 * 
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun Toolbar.itemClicks(): Observable<MenuItem> = RxToolbar.itemClicks(this)
