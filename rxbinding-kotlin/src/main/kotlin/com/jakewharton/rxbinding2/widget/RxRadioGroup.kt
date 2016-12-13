package com.jakewharton.rxbinding2.widget

import android.widget.RadioGroup
import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * Create an observable of the checked view ID changes in `view`.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 *
 * *Note:* A value will be emitted immediately on subscribe.
 */
inline fun RadioGroup.checkedChanges(): Observable<Int> = RxRadioGroup.checkedChanges(this)

/**
 * An action which sets the checked child of `view` with ID. Passing {@code -1} will clear
 * any checked view.
 *
 * *Warning:* The created observable keeps a strong reference to `view`. Unsubscribe
 * to free this reference.
 */
inline fun RadioGroup.checked(): Consumer<in Int> = RxRadioGroup.checked(this)
