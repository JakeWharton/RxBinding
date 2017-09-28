@file:Suppress(
    names = "NOTHING_TO_INLINE"
)

package com.jakewharton.rxbinding2.widget

import android.support.annotation.CheckResult
import android.widget.CompoundButton
import com.jakewharton.rxbinding2.InitialValueObservable
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
@CheckResult
inline fun CompoundButton.checkedChanges(): InitialValueObservable<Boolean> = RxCompoundButton.checkedChanges(this)

/**
 * An action which sets the checked property of `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
inline fun CompoundButton.checked(): Consumer<in Boolean> = RxCompoundButton.checked(this)

/**
 * An action which sets the toggles property of `view` with each value.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
inline fun CompoundButton.toggle(): Consumer<in Any> = RxCompoundButton.toggle(this)
