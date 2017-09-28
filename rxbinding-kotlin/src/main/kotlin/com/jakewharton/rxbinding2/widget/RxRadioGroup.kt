@file:Suppress(
    names = "NOTHING_TO_INLINE"
)

package com.jakewharton.rxbinding2.widget

import android.support.annotation.CheckResult
import android.widget.RadioGroup
import com.jakewharton.rxbinding2.InitialValueObservable
import io.reactivex.functions.Consumer

/**
 * Create an observable of the checked view ID changes in `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
@CheckResult
inline fun RadioGroup.checkedChanges(): InitialValueObservable<Int> = RxRadioGroup.checkedChanges(this)

/**
 * An action which sets the checked child of `view` with ID. Passing {@code -1} will clear
 * any checked view.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
@CheckResult
inline fun RadioGroup.checked(): Consumer<in Int> = RxRadioGroup.checked(this)
