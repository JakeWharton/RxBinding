package com.jakewharton.rxbinding.widget

import android.widget.AutoCompleteTextView
import rx.Observable
import rx.functions.Action1

/**
 * Create an observable of item click events on `view`.
 * 
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun AutoCompleteTextView.itemClickEvents(): Observable<AdapterViewItemClickEvent> = RxAutoCompleteTextView.itemClickEvents(this)
