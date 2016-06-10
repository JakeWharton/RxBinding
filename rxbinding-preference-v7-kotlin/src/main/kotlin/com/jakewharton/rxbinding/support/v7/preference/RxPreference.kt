package com.jakewharton.rxbinding.support.v7.preference

import android.support.v7.preference.Preference
import rx.Observable
import rx.functions.Action1

/**
 * Create an observable which emits on `Preference` click events. The emitted value is
 * unspecified and should only be used as notification.
 * 
 * *Warning:* The created observable keeps a strong reference to `preference`. Unsubscribe
 * to free this reference.
 * *Warning:* The created observable uses [Preference.setOnPreferenceClickListener] to observe
 * clicks. Only one observable can be used for a preference at a time.
 */
public inline fun Preference.clicks(): Observable<Unit> = RxPreference.clicks(this).map { Unit }

/**
 * Create an observable which emits on `Preference` change events.
 * 
 * *Warning:* The created observable keeps a strong reference to `preference`. Unsubscribe
 * to free this reference.
 * *Warning:* The created observable uses [Preference.setOnPreferenceChangeListener] to observe
 * changes. Only one observable can be used for a preference at a time.
 */
public inline fun Preference.changes(): Observable<Any> = RxPreference.changes(this)
