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

/**
 * An action that sets the optional hint text that is displayed at the bottom of the the matching
 * list. This can be used as a cue to the user on how to best use the list, or to provide extra
 * information.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun AutoCompleteTextView.completionHint(): Action1<in CharSequence> = RxAutoCompleteTextView.completionHint(this)

/**
 * An action that specifies the minimum number of characters the user has to type in the edit box
 * before the drop down list is shown. When threshold is less than or equals 0, a threshold of 1
 * is applied.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
public inline fun AutoCompleteTextView.threshold(): Action1<in Int> = RxAutoCompleteTextView.threshold(this)
