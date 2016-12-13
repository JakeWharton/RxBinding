package com.jakewharton.rxbinding2.widget

import android.widget.CompoundButton
import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * Create an observable of booleans representing the checked state of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Warning:* The created observable uses [CompoundButton.setOnCheckedChangeListener]
 * to observe checked changes. Only one observable can be used for a view at a time.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
inline fun CompoundButton.checkedChanges(): Observable<Boolean> = RxCompoundButton.checkedChanges(this)

/**
 * An action which sets the checked property of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun CompoundButton.checked(): Consumer<in Boolean> = RxCompoundButton.checked(this)

/**
 * An action which sets the toggles property of `view` with each value.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun CompoundButton.toggle(): Consumer<in Any> = RxCompoundButton.toggle(this)
